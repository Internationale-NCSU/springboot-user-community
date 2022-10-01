package com.dareway.jc.content.community.pojo.Topic;

import com.dareway.jc.content.community.pojo.Article.ArticlePagesOfTopicModule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * <p>
 *  封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 *
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicModule {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long topicId;
    private String title;
    private String coverUrl;
    private List<ArticlePagesOfTopicModule> articlePages;
}
