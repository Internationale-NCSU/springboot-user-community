package com.dareway.jc.content.community.pojo.Article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleMsg {
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;
    private Integer readNumber;
    private Integer likeNumber;
    private String coverImageUrl;
    private String isLike;
}
