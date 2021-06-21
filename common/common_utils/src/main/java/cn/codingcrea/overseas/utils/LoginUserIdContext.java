package cn.codingcrea.overseas.utils;

import cn.codincrea.overseas.model.User;
import org.springframework.stereotype.Component;

/**
 * 用户容器，用于代替session
 */
@Component
public class LoginUserIdContext {

    private ThreadLocal<Integer> userIds = new ThreadLocal<>();

    public void setUserId(Integer id) {
        userIds.set(id);
    }

    public Integer getUserId() {
        return userIds.get();
    }

    public void clearId() {
        userIds.remove();
    }
}