package com.dareway.jc.content.community.service;


import com.dareway.jc.content.community.domain.R;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lichp
 * @author WangPx
 * @since 2021-03-23
 * @since 2021-04-09
 */
public interface ConCollectService {
    R<?> addToCollection(Long articleId);

    R<?> selectAllCollection(String userId);

    boolean isCollectArticle(Long articleId);
}
