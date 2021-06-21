package cn.codingcrea.overseas.controller;

import cn.codincrea.overseas.model.User;
import cn.codingcrea.overseas.service.UserService;
import cn.codingcrea.overseas.utils.JWTUtil;
import cn.codingcrea.overseas.utils.Project4Constant;
import cn.codingcrea.overseas.utils.Project4Utils;
import cn.codingcrea.overseas.utils.RSAUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/user")
public class LoginController implements Project4Constant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${rsa.privatekey}")
    private String privateKey;

    //Content-Type:application/x-www-form-urlencoded
    @PostMapping(value = "/testRSA", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String test(@RequestParam("password") String password) throws Exception {
        System.out.println(password);
        byte[] bytes = RSAUtil.decryptByPrivateKey(password, privateKey);
        return Project4Utils.getJSONString(CODE_SUCCESS, "解密成功！", "uncrypted",new String(bytes));
    }

    @GetMapping("/testdemo")
    public String testdemo2() {
        return "/demo";
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String register(@RequestBody User user){
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            String msg = "注册成功，我们已向您的邮箱发送了激活邮件，请尽快激活！";
            return Project4Utils.getJSONString(CODE_SUCCESS, msg, null);
        } else {
            return Project4Utils.getJSONString(CODE_FAILURE, null, map);
        }
    }

    @PostMapping(value = "/registerS", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String hello(@RequestBody User user) {
        try {
            String password = new String(RSAUtil.decryptByPrivateKey(user.getPassword(), privateKey));
            user.setPassword(password);
            return register(user);
        } catch (Exception e) {
            return Project4Utils.getJSONString(CODE_SERVER_FAILURE, "服务器解密失败，请重试！", null);
        }
    }

    @GetMapping(value = "/activation/{userId}/{activationCode}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String activation(@PathVariable("userId") int userId, @PathVariable("activationCode") String activationCode) {
        int result = userService.activation(userId, activationCode);
        int code = CODE_SUCCESS;
        String msg = "";
        if(result == ACTIVATION_SUCCESS) {
            code = CODE_SUCCESS;
            msg = "激活成功，您的账号已经可以正常使用了！";
        } else if(result == ACTIVATION_REPEATE) {
            code = CODE_FAILURE;
            msg = "该账号已被激活！";
        } else {
            code = CODE_FAILURE;
            msg = "激活失败，您提供的激活码不对！";
        }
        return Project4Utils.getJSONString(code, msg, null);
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //验证码存入session
        session.setAttribute("kaptcha", text);
        //图片输出给浏览器，不用关闭流，springmvc会自动做
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            //javax的用于图片输出的工具
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(@RequestBody Map<String, String> params, HttpSession session,HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        String email = params.get("email");
        String password = params.get("password");
        String code = params.get("code");
        boolean rememberme = params.get("rememberme").equals("false");

        //验证码
        String kaptcha = (String) session.getAttribute("kaptcha");

        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            Map<String, Object> map = new HashMap<>();
            map.put("codeMsg", "验证码不正确！");
            return Project4Utils.getJSONString(CODE_FAILURE, null, map);
        }

        //账号密码
        int expiredSeconds = rememberme ? REMEMBERME_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(email, password);
        if(map.containsKey("loginUser")) {

            User loginUser = (User)map.get("loginUser");
            Map<String, String> payload = new HashMap<>();
            payload.put("id", loginUser.getId().toString());
            String token = JWTUtil.getToken(payload, expiredSeconds);

            map = new HashMap<>();
            map.put("token", token);

            //login时就先存一下认证信息
//            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, loginUser.getPassword(),
//                    loginUser.getAuthorities());
//            SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

            return Project4Utils.getJSONString(CODE_SUCCESS, "成功登录！", map);
        } else {
            return Project4Utils.getJSONString(CODE_FAILURE, null, map);
        }
    }

    @PostMapping(value = "/loginS", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String loginS(@RequestBody Map<String, String> params, HttpSession session, HttpServletResponse response) {
        try{
            String password = params.get("password");
            password = new String(RSAUtil.decryptByPrivateKey(password, privateKey));
            params.put("password", password);
            return login(params, session,response);
        } catch (Exception e) {
            return Project4Utils.getJSONString(CODE_SERVER_FAILURE, "服务器解密失败，请重试！", null);
        }
    }

    @GetMapping(value = "/logout/auth", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        return Project4Utils.getJSONString(CODE_SUCCESS, "已退出登录！", null);
    }
}