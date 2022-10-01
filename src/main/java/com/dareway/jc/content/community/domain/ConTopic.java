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
@ApiModel(value = "ConTopic对象", description = "")
public class ConTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号 分类id")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long topicId;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "注释 分类备注")
    private String des;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否显示0/1")
    private String enable;

    private String deleteFlag;

    @ApiModelProperty(value = "创建者userId")
    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String coverUrl;
}
