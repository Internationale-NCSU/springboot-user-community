package com.dareway.jc.content.community.pojo.Article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
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

public class ArticlePage {
    //ca.title,ca.read_number,ca.topic_id,ca.id,ui.nickname,ui.photo_url
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Integer readNumber;
    private String nickname;
    private String photoUrl;
    private String coverImageUrl;
    private String content;
    private Boolean isLikeToArticle;
    private Integer likeNumber;
    private Integer commentNumber;
    private List titlesAndTopicIds;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    private String formatTime;
    private String videoUrl;
    private String userId;
    private String videoUrls;
    private String countyCode;
    private String isWebUrl;
}
