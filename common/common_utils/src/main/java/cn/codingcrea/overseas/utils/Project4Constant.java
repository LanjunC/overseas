package cn.codingcrea.overseas.utils;

public interface Project4Constant {

    /**
     * code-成功
     */
    int CODE_SUCCESS = 0;

    /**
     * code-失败
     */
    int CODE_FAILURE = 1;

    /**
     * code-未登录
     */
    int CODE_NO_LOGIN = 2;

    /**
     * code-权限不足
     */
    int CODE_DENIED = 3;

    /**
     * code-服务器错误
     */
    int CODE_SERVER_FAILURE = 4;

    /**
     * 激活-成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 激活-重复
     */
    int ACTIVATION_REPEATE = 1;

    /**
     * 激活-提供的激活码不正确
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认登录时间-1天
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 24;

    /**
     * “记住我”时的登录时间—30天
     */
    int REMEMBERME_EXPIRED_SECONDS = 3600 * 24 * 30;


    /**
     * 帖子列表显示的帖子数
     */
    int DISCUSSPOSTS_PER_PAGE = 10;

    /**
     * 实体类型-帖子
     */
    int ENTITY_TYPE_POST = 0;

    /**
     * 实体类型-评论
     */
    int ENTITY_TYPE_COMMENT = 1;


}
