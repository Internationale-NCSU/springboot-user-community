package com.dareway.jc.content.community.security;

import com.dareway.jc.common.domain.User;
import com.dareway.jc.content.community.utils.RedisClient;
import com.dareway.jc.content.community.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dareway.jc.content.community.constant.CacheConstants.LOGIN_TOKEN_KEY;

/**
 * 安全服务
 *
 * @author lichp
 * @version 1.0.0  2020/10/27 15:40
 * @since JDK1.8
 */
@Component
public class SecurityService {

    @Autowired
    private RedisClient redisClient;

    /**
     * 获取当前登录用户
     *
     * @return com.dareway.jc.common.domain.User
     * @author lichp
     * @version 1.0.0  2020/10/28 10:10
     * @since JDK1.8
     */
    public User getLoginUser() {
        String token = ServletUtils.getToken();
        return (User) redisClient.get(LOGIN_TOKEN_KEY + token);
    }

    /**
     * 获取当前用户登录ID
     *
     * @return java.lang.Integer
     * @author lichp
     * @version 1.0.0  2020/10/28 10:10
     * @since JDK1.8
     */
    public String getLoginUserId() {
        return getLoginUser().getUserid();
    }
}
