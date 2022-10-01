package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConComment;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/community/conComment")
@Api(tags = " 评论相关接口 ")
public class ConCommentController {
    @Autowired
    private ConCommentService conCommentService;

    @PutMapping("createCommentToArticle")
    @ApiOperation(value = "发表对文章/视频的评论", httpMethod = "PUT")
    public R<?> createCommentToArticle(@RequestParam(value = "articleId") Long articleId,
                                       @RequestParam(value = "content") String content) {
        return conCommentService.createCommentToArticle(articleId, content);
    }

    @PutMapping("createCommentAsReply")
    @ApiOperation(value = "回复评论的评论", httpMethod = "PUT")
    public R<?> createCommentAsReply(@RequestParam(value = "pid") Long pid,
                                     @RequestParam(value = "content") String content) {
        return conCommentService.createCommentAsReply(pid, content);
    }

    @PutMapping("editArticle")
    @ApiOperation(value = "编辑评论", httpMethod = "PUT")
    public R<?> editArticle(ConComment conComment) {
        return conCommentService.editComment(conComment);
    }

    @PutMapping("deleteArticle")
    @ApiOperation(value = "删除评论", httpMethod = "PUT")
    public R<?> deleteArticle(@RequestParam(value = "id") Long id) {
        return conCommentService.deleteComment(id);
    }

    @PostMapping("/selectLikeNumberById")
    @ApiOperation(value = "统计评论赞数", httpMethod = "POST")
    public R<?> selectLikeNumberById(@RequestParam(value = "id") Long id) {
        return conCommentService.selectLikeNumberById(id);
    }

    @PostMapping("/selectCommentById")
    @ApiOperation(value = "根据id查询评论与其回复", httpMethod = "POST")
    public R<?> selectCommentById(@RequestParam(value = "id") Long commentId,
                                  @RequestParam(required = false, defaultValue = "1") Integer current,
                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return conCommentService.selectCommentById(commentId, current, size);
    }


}

