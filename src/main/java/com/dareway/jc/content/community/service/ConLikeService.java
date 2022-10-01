package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.ConLike;
import com.dareway.jc.content.community.domain.R;

import java.util.Map;

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
public interface ConLikeService {

    R<Map<String, Object>> likeToComment(Long commentId);

    R<Map<String, Object>> likeToArticle(Long articleId);

    boolean isLikeToComment(Long commentId);

    boolean isLikeToArticle(Long commentId);

}
