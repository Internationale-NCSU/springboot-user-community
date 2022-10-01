package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.Article.SelectArticlesConditions;
import com.dareway.jc.content.community.service.ConManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/ConManagement")
@Api(tags = "管理用功能")
public class ConManagementController {
    @Autowired
    ConManagementService conManagementService;

    @PostMapping("selectArticlesByCondition")
    @ApiOperation(value = "根据条件查询文章",httpMethod = "POST")
    public R<?> selectArticlesByCondition( SelectArticlesConditions sac ){
        return conManagementService.selectArticlesByCondition(sac);
    }
}
