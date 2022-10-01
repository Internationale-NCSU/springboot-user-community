package com.dareway.jc.content.community.constant;

/**
 * 缓存的key 常量
 * @author  lichp
 * @version 1.0.0  2020/10/20 11:07
 * @since JDK1.8
 */
public class CacheConstants {
    /**
     * 令牌自定义标识
     */
    public static final String HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";


    /**
     * 二维码状态前缀
     */
    public final static String QR_CODE_STATUS_PREFIX = "QR_code_status_prefix:";

    public final static String QR_CODE_SUCCESS_PREFIX = "qr_code_success_prefix:";

    public final static String VERIFY_CODE_PREFIX = "verify_code_prefix:";



    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";


    public static final String ORDER_PREFIX = "order_id:";
}
