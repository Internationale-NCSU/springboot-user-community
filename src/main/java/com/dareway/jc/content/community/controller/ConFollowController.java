package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.service.ConFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/conFollow")
@Api(tags = "关注粉丝相关接口")
public class ConFollowController {
    @Autowired
    private ConFollowService conFollowService;

    @PutMapping("/followOrUnfollowTheAuthor")
    @ApiOperation(value = "关注或取关作者", httpMethod = "PUT")
    public R<?> followOrUnfollowTheAuthor(@RequestParam(value = "id") String id) {
        return conFollowService.followOrUnfollowTheAuthor(id);
    }

    @PostMapping("/selectUserFollowList")
    @ApiOperation(value = "查询用户关注作者列表", httpMethod = "POST")
    public R<?> selectUserFollowList() {
        return conFollowService.selectUserFollowList();
    }

    @PostMapping("/selectUserFollowerList")
    @ApiOperation(value = "查询用户粉丝列表", httpMethod = "POST")
    public R<?> selectUserFollowerList() {
        return conFollowService.selectUserFollowerList();
    }

}
