package com.dareway.jc.content.community.pojo.HomePageContent;

import lombok.Data;

import java.util.List;

@Data
public class SelectHeaderAndContentParam {
    private Integer current;
    private Integer size;
    private String headerModuleType;
    private String countyCode;
}
