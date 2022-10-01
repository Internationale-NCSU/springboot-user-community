package com.dareway.jc.content.community.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
public interface ConTopicMapper extends BaseMapper<ConTopic> {

    List<ConArticle> selectArticlesUnderCurrentTopic(Long topicId);

    Page<ConArticle> selectPagingAllArticlesUnderCurrentTopic(@Param("page") Page<ConArticle> page,@Param("para") Long topicId);

    Integer selectAllArticlesUnderCurrentTopic(Long topicId);
}
