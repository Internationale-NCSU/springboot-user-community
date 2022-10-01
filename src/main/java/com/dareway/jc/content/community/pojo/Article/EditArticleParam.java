package com.dareway.jc.content.community.pojo.Article;

import lombok.Data;

import java.util.List;

@Data
public class EditArticleParam {
    private Long articleId;
    private String title;
    private String subtitle;
    private String content;
    private List<Long> topicIds;
    private String draftStatus;
}
