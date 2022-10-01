package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community/conFunction")
@Api(tags = " 功能标签相关接口 ")
public class ConFunctionController {
    @Autowired
    private ConFunctionService conFunctionService;

    @PutMapping("addFunction")
    @ApiOperation(value = "添加功能标签", httpMethod = "PUT")
    public R<?> addFunction(@RequestParam() String funcName, @RequestParam() String tagUrl) {
        return conFunctionService.addFunction(funcName, tagUrl);
    }

    @PutMapping("deleteFunction")
    @ApiOperation(value = "删除功能标签", httpMethod = "PUT")
    public R<?> deleteFunction(@RequestParam() Long id) {
        return conFunctionService.deleteFunction(id);
    }

    @PutMapping("editFunction")
    @ApiOperation(value = "编辑功能标签", httpMethod = "PUT")
    public R<?> editFunction(@RequestParam() Long id, String funcName, String tagUrl,String pageName, String params) {
        return conFunctionService.editFunction(id, funcName, tagUrl,pageName,params);
    }

    @PostMapping("selectFunctionList")
    @ApiOperation(value = "查询全部功能列表", httpMethod = "POST")
    public R<?> selectFunctionList() {
        return conFunctionService.selectFunctionList();
    }

    @PostMapping("selectGroupFunctionList")
    @ApiOperation(value = "查询分组部分功能列表", httpMethod = "POST")
    public R<?> selectGroupFunctionList(@RequestBody() List<Long> id) {
        return conFunctionService.selectGroupFunctionList(id);
    }

}
