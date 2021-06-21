package cn.codingcrea.overseas.controller;

import cn.codincrea.overseas.model.User;
import cn.codingcrea.overseas.service.UserService;
import cn.codingcrea.overseas.utils.LoginUserIdContext;
import cn.codingcrea.overseas.utils.Project4Utils;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inner/user")
public class InnerUserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/userInfo/{userId}", produces = "application/json;charset=UTF-8")
    public String getUserInfo(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        user.setPassword(null);
        return JSONObject.toJSONString(user);
    }

}
