package com.dareway.jc.content.community.pojo.Article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
@Data
public class CreateArticleParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;
    private String type;
    private String title;
    private String subtitle;
    private String content;
    private String videoUrl;
    private List<Long> topicIds;
    private String draftStatus;
    private String coverUrl;
    private String countyCode;
    private String isWebUrl;
}
