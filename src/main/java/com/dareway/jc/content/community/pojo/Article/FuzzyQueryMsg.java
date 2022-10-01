package com.dareway.jc.content.community.pojo.Article;

import com.dareway.jc.content.community.pojo.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FuzzyQueryMsg {
    private String title;
    private String modifiedTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String formatTime;
    private String coverUrl;
    private Integer commentNumber;
    private Integer readNumber;
    private String content;
    private String type;
    private String nickname;
    private String photoUrl;
    private String userId;
    private Integer likeNumber;
    private Boolean isLike;
    private String isWebUrl;
}
