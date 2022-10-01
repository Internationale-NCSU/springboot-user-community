package com.dareway.jc.content.community.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class ConFunction {
    @ApiModelProperty(value = "编号")
    @TableId(type = IdType.ASSIGN_ID)
    Long id;

    @ApiModelProperty(value = "标签图片地址")
    String tagUrl;

    @ApiModelProperty(value = "功能名称")
    String funcName;

    String pageName;

    String params;
}
