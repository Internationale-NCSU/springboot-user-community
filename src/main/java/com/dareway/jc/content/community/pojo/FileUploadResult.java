package com.dareway.jc.content.community.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lichp
 * @version 1.0.0  2020/12/28 9:21
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResult implements Serializable {

    @ApiModelProperty(value = "实际地址 ，http://")
    private String url;

    @ApiModelProperty(value = "逻辑地址 ， 不携带ip")
    private String path;
}
