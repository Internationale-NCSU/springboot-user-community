package com.dareway.jc.content.community.controller;


import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConComment;
import com.dareway.jc.content.community.domain.ConLike;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/community/conLike")
@Api(tags = " 点赞相关接口 ")
public class ConLikeController {
    @Autowired
    private ConLikeService conLikeService;

    @PutMapping("likeToArticle")
    @ApiOperation(value = "对文章/视频点赞", httpMethod = "PUT")
    public R<?> likeToArticle(@RequestParam(value = "articleId") Long articleId) {
        return conLikeService.likeToArticle(articleId);
    }

    @PutMapping("likeToComment")
    @ApiOperation(value = "对评论点赞", httpMethod = "PUT")
    public R<?> likeToComment(@RequestParam(value = "commentId") Long commentId) {
        return conLikeService.likeToComment(commentId);
    }
    @PutMapping("isLikeToArticle")
    @ApiOperation(value = "是否对文章点赞", httpMethod = "PUT")
    public R<?> isLikeToArticle(@RequestParam(value = "articleId") Long articleId) {
        return R.ok(conLikeService.isLikeToArticle(articleId));
    }
    @PutMapping("isLikeToComment")
    @ApiOperation(value = "是否对评论点赞", httpMethod = "PUT")
    public R<?> isLikeToComment(@RequestParam(value = "commentId") Long commentId) {
        return R.ok(conLikeService.likeToComment(commentId));
    }
}

