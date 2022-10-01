package com.dareway.jc.content.community.pojo.Topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TopicMsg {

    private Long topicId;

    private String title;

    private String des;

    private Integer sort;

    private String enable;

    private String deleteFlag;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private String coverUrl;

    private Integer readNumber;
}
