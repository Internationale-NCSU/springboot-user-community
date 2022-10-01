package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.HomePageContent.SelectHeaderAndContentParam;
import com.dareway.jc.content.community.service.ConArticleService;
import com.dareway.jc.content.community.service.ConHomepageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Pinxiang Wang
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/community/conHomepage")
@Api(tags = " 首页图文列表请求接口 ")
public class ConHomepageController {

    @Autowired
    private ConHomepageService conHomepageService;

    @PostMapping("/selectPagingHomePageContent")
    @ApiOperation(value = "分页图文列表请求接口", httpMethod = "POST")
    public R<?> selectPagingHomePageContent(@RequestParam(required = false, defaultValue = "1") Integer current,
                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                            String type,
                                            String countyCode) {
        return conHomepageService.selectPagingHomePageContent(current, size, type,countyCode);
    }

    @PostMapping("/selectPersonalHomePageContent")
    @ApiOperation(value = "个人主页（“我的”）内容请求列表", httpMethod = "POST")
    public R<?> selectPersonalHomePageContent(@RequestParam(required = false, defaultValue = "1") Integer current,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        return conHomepageService.selectPersonalHomePageContent(current, size);
    }

    @PostMapping("/selectUserOriginalContent")
    @ApiOperation(value = "个人原创内容请求列表", httpMethod = "POST")
    public R<?> selectUserOriginalContent(@RequestParam(required = false, defaultValue = "1") Integer current,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam(value = "id") String userId) {
        return conHomepageService.selectUserOriginalContent(current, size, userId);
    }

    @PostMapping("/selectHeaderAndPagingContent")
    @ApiOperation(value = "功能标签与内容请求 1-精选 2-地区", httpMethod = "POST")
    public R<?> selectHeaderAndPagingContent(SelectHeaderAndContentParam s) {
        return conHomepageService.selectHeaderAndPagingContent(s.getCurrent(), s.getSize(), s.getHeaderModuleType(),s.getCountyCode());
    }

}
