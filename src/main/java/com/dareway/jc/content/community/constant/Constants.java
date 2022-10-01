package com.dareway.jc.content.community.constant;

/**
 * 通用常量信息
 *
 * @author lichp
 * @version 1.0.0  2020/10/20 11:07
 * @since JDK1.8
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;


    /**
     * jwt:
     *   secret: XX$^^&^%%VHGZ!@@$#%@#%$%$^VBJHA7938217649&*^GBHU
     *   issuer: lichp.user
     *   expires: 12
     * 令牌有效期（小时）
     */
    public static final String SECRET = "XX$^^&^%%VHGZ!@@$#%@#%$%$^VBJHA7938217649&*^GBHU";
    public static final String ISSUER = "lichp.user";
    public static final int EXPIRES_TIME = 12;

    public static final String TOTAL_COUNT = "total";
    public static final String RESULT_LIST = "result";

    public static final String COMMA = ",";

    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
    public static final String FTP_PREFIX = "/user/java/unicronaged/";
}
