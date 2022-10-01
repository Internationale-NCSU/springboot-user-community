package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.ConComment;
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
public interface ConCommentService {

    R<Map<String, Object>> createCommentToArticle(Long articleId, String content);

    R<Map<String, Object>> createCommentAsReply(Long pid, String content);

    R<Map<String, Object>> editComment(ConComment conComment);

    R<?> deleteComment(Long id);

    R<Map<String, Object>> selectLikeNumberById(Long id);

    R<?> selectCommentById(Long commentId, Integer current, Integer size);
}
