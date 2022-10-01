package com.dareway.jc.content.community.controller;


import com.dareway.jc.content.community.domain.ConTopic;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lichp
 * @author WangPx
 * @since 2021-03-23
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/community/conTopic")
@Api(tags = " 话题分类相关接口 ")
public class ConTopicController {
    @Autowired
    private ConTopicService conTopicService;

    private String userId = "2019091300000000000001088795";

    @PutMapping("addTopic")
    @ApiOperation(value = "添加话题", httpMethod = "PUT")
    public R<Map<String, Object>> addTopic(@RequestParam() String title, @RequestParam() String des,String coverUrl) {
        return conTopicService.addTopic(title, des,coverUrl);
    }

    @PutMapping("editTopic")
    @ApiOperation(value = "编辑话题", httpMethod = "PUT")
    public R<Map<String, Object>> editTopic(String title, String des, Integer sort,String coverUrl) {
        return conTopicService.editTopic(title, des, sort,coverUrl);
    }

    @PutMapping("deleteTopic")
    @ApiOperation(value = "删除话题", httpMethod = "PUT")
    public R<Map<String, Object>> deleteTopic(@RequestParam() Long topicId) {
        return conTopicService.deleteTopic(topicId);
    }

    @PutMapping("migrateTopic")
    @ApiOperation(value = "旧话题迁移", httpMethod = "PUT")
    public R<Map<String, Object>> migrateTopic() {
        return conTopicService.migrateTopic();
    }

    @PostMapping("selectAllTopic")
    @ApiOperation(value = "请求话题列表", httpMethod = "POST")
    public R<?> selectAllTopic() {
        return R.ok(conTopicService.selectAllTopic());
    }

    @PostMapping("selectAllArticlesUnderCurrentTopic")
    @ApiOperation(value = "请求当前话题下全部文章", httpMethod = "POST")
    public R<?> selectAllArticlesUnderCurrentTopic(@RequestParam(required = false, defaultValue = "1") Integer current,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam(value = "topicId") Long topicId) {
        return conTopicService.selectAllArticlesUnderCurrentTopic(current,size,topicId);
    }

    @PostMapping("countCurrentTopicReadNumber")
    @ApiOperation(value = "计算当前话题浏览量", httpMethod = "POST")
    public R<?> countCurrentTopicReadNumber(@RequestParam(value = "topicId") Long topicId) {
        return conTopicService.countCurrentTopicReadNumber(topicId);
    }
}

