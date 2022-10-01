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

/**
 * <p>
 *
 * </p>
 *
 * @author lichp
 * @author WangPx
 * @since 2021-03-23
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ConComment对象", description = "")
public class ConComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论编号")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)

    private Long id;

    @ApiModelProperty(value = "评论所在文章id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;

    @ApiModelProperty(value = "记录回复的评论的ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论点赞数")
    private Integer likeNumber;

    @ApiModelProperty(value = "是否被删除")
    private String deleteFlag;

    @ApiModelProperty(value = "0--未读，1--已读")
    private String readFlag;

    @ApiModelProperty(value = "评论人id")
    private String creator;

    @ApiModelProperty(value = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updater;

    private Date updateTime;

}
