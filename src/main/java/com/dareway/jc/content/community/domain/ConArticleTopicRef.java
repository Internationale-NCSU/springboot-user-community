package com.dareway.jc.content.community.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ConCollect对象", description = "")
public class ConArticleTopicRef implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "所属类别")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long topicId;

    @ApiModelProperty(value = "所属类别")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
