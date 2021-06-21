package cn.codingcrea.overseas.controller;

import cn.codincrea.overseas.model.User;
import cn.codingcrea.overseas.service.UserService;
import cn.codingcrea.overseas.utils.LoginUserIdContext;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import cn.codingcrea.overseas.utils.RSAUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequestMapping("/api/user/auth")
public class UserController implements Project4Constant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginUserIdContext loginUserIdContext;

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyid;

    @Value("${aliyun.oss.file.secretid}")
    private String secretid;

    @Value("${aliyun.oss.file.bucket.name}")
    private String bucket;

    @Value("${aliyun.oss.file.bucket.dir}")
    private String dirName;

    @Value("${rsa.privatekey}")
    private String privateKey;

    @GetMapping(value = "/myInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserInfo() {
        int id = loginUserIdContext.getUserId();
        System.out.println(id);
        User user = userService.getUserById(id);
        user.setPassword(null);
        return Project4Utils.getJSONString(0, null, "loginUser", user);
    }

    @PutMapping(value = "/myInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setUserInfo(@RequestBody User user) {
        int id = loginUserIdContext.getUserId();
        userService.updateInfo(id, user);
        return Project4Utils.getJSONString(CODE_SUCCESS, null, null);
    }

    /**
     * 更新头像到Oss
     * @param headerimage
     * @return
     */
    @PutMapping(value = "/avatar", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String uploadHeaderOss(@RequestParam("file") MultipartFile headerimage) {
        if(headerimage == null) {
            return Project4Utils.getJSONString(CODE_FAILURE, "您未选择图片！", null);
        }

        String filename = headerimage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));  //.png等后缀
        if(StringUtils.isBlank(suffix)) {
            return Project4Utils.getJSONString(CODE_FAILURE, "文件格式不正确！", null);
        }

        //生成随机访问url
        filename = Project4Utils.generateUUID() + suffix;

        //文件名加上头像所在文件夹名
        filename = dirName + "/" + filename;

        //上传头像
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, secretid);
        try(
                InputStream is = headerimage.getInputStream();
        ) {
            ossClient.putObject(bucket, filename, is);
            ossClient.shutdown();
        } catch (IOException e) {
            logger.error("上传文件失败:" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常！", e);
        }

        //更新headerUrl(web访问路径)
        //文件位置(web访问路径)http://${bucket}.oss-cn-shanghai.aliyuncs.com/${dirName}/${filename}
        int userId = loginUserIdContext.getUserId();
        String headerUrl = "http://" + bucket + "." + endpoint + "/" + filename;        //服务器实际存放头像位置
        userService.updateHeader(userId, headerUrl);

        return Project4Utils.getJSONString(CODE_SUCCESS, "上传头像成功！", null);
    }

    @PutMapping(value = "/password", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String password = params.get("password");
        User loginUser = userService.getUserById(loginUserIdContext.getUserId());
        if(!loginUser.getPassword().equals(Project4Utils.md5(oldPassword))) {
            return Project4Utils.getJSONString(CODE_FAILURE, "原密码错误！", null);
        }
        int id = loginUser.getId();
        userService.updatePassword(id, password);
        return Project4Utils.getJSONString(CODE_SUCCESS, "密码修改成功！", null);
    }

    @PutMapping(value = "/passwordS", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String changePasswordS(@RequestBody Map<String, String> params) {
        try {
            String oldPassword = params.get("oldPassword");
            String password = params.get("password");
            oldPassword = new String(RSAUtil.decryptByPrivateKey(oldPassword, privateKey));
            password = new String(RSAUtil.decryptByPrivateKey(password, privateKey));
            params.put("oldPassword",oldPassword);
            params.put("password",password);

        } catch (Exception e) {
            return Project4Utils.getJSONString(CODE_SERVER_FAILURE, "服务器解密失败，请重试！", null);
        }
        return changePassword(params);


    }
}
