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
@ApiModel(value = "ConLike对象", description = "")
public class ConLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号 点赞记录id")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "点赞文章id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;

    @ApiModelProperty(value = "点赞评论id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commentId;

    @ApiModelProperty(value = "0-未读，1-已读")
    private String readFlag;

    @ApiModelProperty(value = "创建人 点赞者userid")
    private String creator;

    @ApiModelProperty(value = "创建日期 点赞时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
