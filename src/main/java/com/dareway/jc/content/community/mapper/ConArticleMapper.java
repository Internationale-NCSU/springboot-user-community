package com.dareway.jc.content.community.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dareway.jc.content.community.domain.ConTopic;
import com.dareway.jc.content.community.pojo.Article.ArticlePage;
import com.dareway.jc.content.community.pojo.Article.OldArticle;
import com.dareway.jc.content.community.pojo.Article.SelectArticlesConditions;
import com.dareway.jc.content.community.pojo.Management.SearchArticlePage;
import com.dareway.jc.content.community.pojo.Topic.TitlesAndTopicId;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lichp
 * @author WangPx
 * @since 2021-03-23
 * @since 2021-04-09
 */
public interface ConArticleMapper extends BaseMapper<ConArticle> {

    Page<ConArticle> selectUserInfoAndArt(@Param("page") Page<ConArticle> page, @Param("params") ConArticle conArticle);

    Map<String, Object> selectNicknameAndProfileByUserId(@Param("userId") String userId);

    Page<ArticlePage> selectPagingHomePageContent(@Param("page") Page<ArticlePage> page,@Param("type") String type,@Param("countyCode")String countyCode);

    List<OldArticle> selectArticleFromOldDataSrc();

    List<TitlesAndTopicId> selectTopicIdsAndTitles(Long articleId);

    List<ConTopic> selectAllTopicByArticleId(Long articleId);

    Page<ArticlePage> selectPagingOriginalArticle(@Param("page") Page<ArticlePage> page, String userId);

    Page<SearchArticlePage> selectArticlesByCondition( @Param("page") Page<SearchArticlePage> page,@Param("para") SelectArticlesConditions sac);

    String selectVideoUrlsById(Long id);

}
