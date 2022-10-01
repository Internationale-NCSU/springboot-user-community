package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.R;

import java.util.List;
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
public interface ConTopicService {

    R<Map<String, Object>> addTopic(String title, String des,String coverUrl);

    R<Map<String, Object>> editTopic(String title, String des, Integer sort, String coverUrl);

    R<Map<String, Object>> deleteTopic(Long topicId);

    List selectAllTopic();

    R<?> selectAllArticlesUnderCurrentTopic(Integer current, Integer size, Long topicId);

    R<Map<String, Object>> migrateTopic();

    R<?> countCurrentTopicReadNumber(Long topicId);
}
