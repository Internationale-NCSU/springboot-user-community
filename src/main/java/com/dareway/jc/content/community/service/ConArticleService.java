package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.Commnet.ReplyMsg;

import java.util.List;

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
public interface ConArticleService {

    R<?> save(ConArticle conArticle);

    R<?> createArticle(Long articleId, String type, String title, String subtitle, String content, String videoUrl, List<Long> topicIds, String draftStatus, String coverUrl, String countyCode, String isWebUrl);

    R<?> editArticle(Long articleId, String title, String subtitle, String content, List<Long> topicIds, String draftStatus,String countyCode);

    R<?> deleteArticle(Long articleId);

    R<?> deleteDraft(Long articleId);

    R<?> selectLikeNumberById(Long id);

    R<?> selectCommentNumberById(Long id);

    R<?> selectArticleContent(Long articleId);

    R<?> selectArticleAllComments(Long articleId, Integer current, Integer size);

    R<?> selectArticleTitleById(Long id);

    void selectChildrenReply(Long id, List<ReplyMsg> children);

    R<?> articleMigration();

    R<?> selectArticleByTitle(String title);

    R<?> selectAllTopicByArticleId(Long id);

    R<?> selectDraftByType(String type);

    R<?> imageUpload();
}
