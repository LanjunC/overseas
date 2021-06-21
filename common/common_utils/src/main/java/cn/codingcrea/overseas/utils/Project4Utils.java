package cn.codingcrea.overseas.utils;

import cn.codincrea.overseas.model.User;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

public class Project4Utils {
    //生成不含“-”的随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5加密
    public static String md5(String s) {
        if(StringUtils.isBlank(s)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }

    /**
     * 生成json数据
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 生成json数据
     * @param code
     * @param msg
     * @param key
     * @param value
     * @return
     */
    public static String getJSONString(int code, String msg, String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put(key, value);
        return jsonObject.toJSONString();
    }

}
