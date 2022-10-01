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
import java.util.List;

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
@ApiModel(value = "ConArticle对象", description = "")
public class ConArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

//    @ApiModelProperty(value = "所属类别")
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    private Long topicId;

    @ApiModelProperty(value = "类型  图文/视频")
    private String type;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "封面图片URL")
    private String coverUrl;

    @ApiModelProperty(value = "视频类型URL")
    private String videoUrl;

    @ApiModelProperty(value = "点赞数")
    private Integer likeNumber;

    @ApiModelProperty(value = "评论量")
    private Integer commentNumber;

    @ApiModelProperty(value = "浏览量")
    private Integer readNumber;

    @ApiModelProperty(value = "删除")
    private String deleteFlag;

    @ApiModelProperty(value = "创建人编号")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "修改人")
    private String updater;

    @ApiModelProperty(value = "编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "草稿状态")
    private String draftStatus;

    private String videoUrls;

    private String countyCode;

    private String isWebUrl;
}
