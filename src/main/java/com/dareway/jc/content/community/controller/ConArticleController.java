package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.Article.CreateArticleParam;
import com.dareway.jc.content.community.service.ConArticleService;
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
@RequestMapping("/community/conArticle")
@Api(tags = " 文章视频相关接口 ")
public class ConArticleController {

    @Autowired
    private ConArticleService conArticleService;

    @PutMapping("createArticle")
    @ApiOperation(value = "发布文章/视频(1-文章,2-视频,3-微帖)", httpMethod = "PUT")
    public R<?> createArticle(@RequestBody CreateArticleParam createArticleParam) {
        return conArticleService.createArticle(
                createArticleParam.getArticleId(),
                createArticleParam.getType(),
                createArticleParam.getTitle(),
                createArticleParam.getSubtitle(),
                createArticleParam.getContent(),
                createArticleParam.getVideoUrl(),
                createArticleParam.getTopicIds(),
                createArticleParam.getDraftStatus(),
                createArticleParam.getCoverUrl(),
                createArticleParam.getCountyCode(),
                createArticleParam.getIsWebUrl()
        );
    }

//    @PutMapping("editArticle")
//    @ApiOperation(value = "编辑文章/视频",httpMethod = "PUT")
//    public R<?> editArticle(@RequestBody EditArticleParam editArticleParam){
//        return conArticleService.editArticle(
//                editArticleParam.getArticleId(),
//                editArticleParam.getTitle(),
//                editArticleParam.getSubtitle(),
//                editArticleParam.getContent(),
//                editArticleParam.getTopicIds(),
//                editArticleParam.getDraftStatus());
//    }

    @PutMapping("deleteArticle")
    @ApiOperation(value = "删除文章/视频", httpMethod = "PUT")
    public R<?> deleteArticle(@RequestParam(value = "articleId") Long articleId) {
        return conArticleService.deleteArticle(articleId);
    }

    @PutMapping("deleteDraft")
    @ApiOperation(value = "删除草稿（不可低用来删除文章）", httpMethod = "PUT")
    public R<?> deleteDraft(@RequestParam(value = "articleId") Long articleId) {
        return conArticleService.deleteDraft(articleId);
    }

    @PutMapping("articleMigration")
    @ApiOperation(value = "旧文章迁移", httpMethod = "PUT")
    public R<?> articleMigration() {
        return R.ok("暂停使用，需要时重新配置");
    }//方法在conArticleService.articleMigration()

    @PostMapping("/selectLikeNumberById")
    @ApiOperation(value = "统计文章点赞数", httpMethod = "POST")
    public R<?> selectLikeNumberById(@RequestParam(value = "id") Long id) {
        return conArticleService.selectLikeNumberById(id);
    }

    @PostMapping("/selectCommentNumberById")
    @ApiOperation(value = "统计文章评论数", httpMethod = "POST")
    public R<?> selectCommentNumberById(@RequestParam(value = "id") Long id) {
        return conArticleService.selectCommentNumberById(id);
    }

    @PostMapping("/selectArticleContent")
    @ApiOperation(value = "文章/视频内容请求接口(1-文章,2-视频)", httpMethod = "POST")
    public R<?> selectArticleContent(@RequestParam(value = "articleId") Long articleId) {
        return conArticleService.selectArticleContent(articleId);
    }

    @PostMapping("/selectArticleAllComments")
    @ApiOperation(value = "请求当前文章全部评论", httpMethod = "POST")
    public R<?> selectArticleAllComments(@RequestParam(value = "articleId") Long articleId,
                                         @RequestParam(required = false, defaultValue = "1") Integer current,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return conArticleService.selectArticleAllComments(articleId, current, size);
    }

    @PostMapping("/selectArticleById")
    @ApiOperation(value = "根据id查询文章/视频(网页)", httpMethod = "POST")
    public R<?> selectArticleTitleById(@RequestParam(value = "id") Long id) {
        return conArticleService.selectArticleTitleById(id);
    }

    @PostMapping("/selectArticleByTitle")
    @ApiOperation(value = "根据标题查询文章/视频", httpMethod = "POST")
    public R<?> selectArticleByTitle(@RequestParam(value = "title") String title) {
        return conArticleService.selectArticleByTitle(title);
    }

    @PostMapping("/selectAllTopicByArticleId")
    @ApiOperation(value = "根据id查询当前文章所在全部话题", httpMethod = "POST")
    public R<?> selectAllTopicByArticleId(@RequestParam(value = "id") Long id) {
        return conArticleService.selectAllTopicByArticleId(id);
    }

    @PostMapping("/selectDraftByType")
    @ApiOperation(value = "根据草稿类型查询当前用户草稿内容", httpMethod = "POST")
    public R<?> selectDraftByType(@RequestParam() String type) {
        return conArticleService.selectDraftByType(type);
    }

    @PostMapping("/imageUploadTest")
    @ApiOperation(value = "文件服务上传测试",httpMethod = "POST")
    public R<?> imageUploadTest(){
        return conArticleService.imageUpload();
    }
}

