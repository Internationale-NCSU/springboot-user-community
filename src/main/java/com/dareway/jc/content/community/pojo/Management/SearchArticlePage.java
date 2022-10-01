package com.dareway.jc.content.community.pojo.Management;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SearchArticlePage {
    Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateDate;
    String creator;
    String nickname;
    String realName;
    Integer likeNumber;
    Integer readNumber;
    Integer commentNumber;
    String title;
    String type;
    String draftStatus;
    String deleteFlag;
}
