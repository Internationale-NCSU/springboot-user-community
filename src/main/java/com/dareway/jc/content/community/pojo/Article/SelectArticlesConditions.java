package com.dareway.jc.content.community.pojo.Article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SelectArticlesConditions {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;

    String createDateStart;

    String createDateEnd;

    String updateDateStart;

    String updateDateEnd;

    String nickname;
    String userId;
    String title;
    Integer likeNumber;
    String type;
    Integer readNumber;
    Integer commentNumber;
    String draftStatus;
    String deleteFlag;
    Integer current;
    Integer size;
}
