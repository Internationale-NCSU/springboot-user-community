package com.dareway.jc.content.community.controller;


import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConCollectService;
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
@RequestMapping("/community/conCollect")
@Api(tags = " 收藏相关接口 ")
public class ConCollectController {
    @Autowired
    private ConCollectService conCollectService;

    @PutMapping("addToCollection")
    @ApiOperation(value = "收藏文章/视频", httpMethod = "PUT")
    public R<?> addToCollection(@RequestParam(value = "articleId") Long articleId) {
        return conCollectService.addToCollection(articleId);
    }

    @PostMapping("selectAllCollection")
    @ApiOperation(value = "查询当前用户收藏列表", httpMethod = "POST")
    public R<?> selectAllCollection(@RequestParam(value = "userId") String userId) {
        return conCollectService.selectAllCollection(userId);
    }

}

