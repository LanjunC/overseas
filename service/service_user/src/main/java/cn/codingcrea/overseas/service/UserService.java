package cn.codingcrea.overseas.service;

import cn.codincrea.overseas.dto.MailSendDto;
import cn.codincrea.overseas.model.User;
import cn.codingcrea.overseas.mailClient.MailFeignClient;
import cn.codingcrea.overseas.mapper.UserMapper;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements Project4Constant{

    @Resource
    private UserMapper userMapper;

    @Value("${project4.path.domain}")
    private String domain;

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.bucket.name}")
    private String bucket;

    @Value("${aliyun.oss.file.bucket.dir}")
    private String dirName;

    @Autowired
    private MailFeignClient mailFeignClient;


    @Autowired
    private TemplateEngine templateEngine;

    public User getUserById(int id) {
        return userMapper.selectById(id);
    }


    public Map<String,Object> register(@NonNull User user) {
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }

        if(StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg","昵称不能为空！");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        //验证邮箱
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        User u = userMapper.selectOne(wrapper);
        if(u != null) {
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }

        //验证昵称
        wrapper.clear();
        wrapper.eq("username", user.getUsername());
        u = userMapper.selectOne(wrapper);
        if(u != null) {
            map.put("usernameMsg","该昵称已存在！");
            return map;
        }

        //注册
        user.setPassword(Project4Utils.md5(user.getPassword()));
        user.setAddtime(new Date());
        user.setActivationCode(Project4Utils.generateUUID());
        user.setAvatar("http://" + bucket + "." + endpoint + "/" + dirName + "/th.jpg");        //服务器实际存放头像位置;

        userMapper.insert(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("url", domain + "api/uer/activation/" + user.getId() + "/" + user.getActivationCode());
        String content  = templateEngine.process("/activation", context);
//        mailClient.sendMail(user.getEmail(), "激活账号", content);  //测试环境用单线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailSendDto mailSendDto = new MailSendDto();
                mailSendDto.setTo(user.getEmail());
                mailSendDto.setSubject("激活账号");
                mailSendDto.setContent(content);
                mailFeignClient.sendMail(mailSendDto);
            }
        }).start(); //由于发送邮件太慢，直接交给多线程去处理

        return map;
    }

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        if(user.getStatus()== 1) {
            return ACTIVATION_REPEATE;
        } else if(user.getActivationCode().equals(activationCode)) {
            user.setStatus(1);
            userMapper.updateById(user);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String,Object> login(String email, String password) {
        Map<String, Object> map = new HashMap<>();

        //空值
        if(StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        //验证
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if(user == null) {
            map.put("emailMsg", "该账号不存在！");
            return map;
        }
        if(user.getStatus() == 0) {
            map.put("emailMsg", "该账号尚未激活！");
            return map;
        }
        password = Project4Utils.md5(password);
        if(!password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误！");
            return map;
        }

        //登录检测通过，返回空map
        map.put("loginUser", user);
        return map;
    }

    public int updateInfo(int id, User user) {
        user.setId(id);
        return userMapper.updateById(user);
    }

    public int updateHeader(int id, String headerUrl) {
        User user = new User();
        user.setAvatar(headerUrl);
        return updateInfo(id, user);
    }

    public int updatePassword(int id, String password) {
        User user = new User();
        user.setPassword(Project4Utils.md5(password));
        return updateInfo(id, user);
    }
}
