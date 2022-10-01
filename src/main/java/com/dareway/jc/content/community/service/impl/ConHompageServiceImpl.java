package com.dareway.jc.content.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConTopic;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConTopicMapper;
import com.dareway.jc.content.community.pojo.Article.ArticlePage;
import com.dareway.jc.content.community.pojo.Article.ArticlePagesOfTopicModule;
import com.dareway.jc.content.community.pojo.Topic.TitlesAndTopicId;
import com.dareway.jc.content.community.pojo.Topic.TopicModule;
import com.dareway.jc.content.community.pojo.User;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.*;
import com.dareway.jc.content.community.utils.DateUtils;
import com.dareway.jc.content.community.utils.IPages;
import com.dareway.jc.content.community.utils.ProfileJudger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ConHompageServiceImpl implements ConHomepageService {
    @Autowired
    private ConArticleService conArticleService;

    @Autowired
    private ConLikeService conLikeService;

    @Autowired
    private ConFollowService conFollowService;

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private ConTopicMapper conTopicMapper;

    @Autowired
    private ConFunctionService conFunctionService;


    @Override
    public R<?> selectPagingHomePageContent(Integer current, Integer size, String type, String countyCode) {
        if (size == null || current == null) {
            current = 1;
            size = 10;
        }
        Page<ArticlePage> page = new Page<>(current, size);
        Page<ArticlePage> conArticlePage = conArticleMapper.selectPagingHomePageContent(page, type, countyCode);
        //给isLike字段赋值
        for (ArticlePage articlePage : conArticlePage.getRecords()) {
            if (ProfileJudger.isNullProfile(articlePage.getPhotoUrl())) {
                articlePage.setPhotoUrl(null);
            }
            if (conLikeService.isLikeToArticle(articlePage.getId())) {
                articlePage.setIsLikeToArticle(true);
            } else {
                articlePage.setIsLikeToArticle(false);
            }
            articlePage.setFormatTime(DateUtils.fromToday(articlePage.getCreateDate()));
            List<TitlesAndTopicId> titlesAndTopicIds = conArticleMapper.selectTopicIdsAndTitles(articlePage.getId());


            if (titlesAndTopicIds != null) {
                articlePage.setTitlesAndTopicIds(titlesAndTopicIds);
            }
            if (("1".equals(type) || "2".equals(type)) && articlePage.getVideoUrls() != null) {
                String videoUrls = articlePage.getVideoUrls();
                String[] s = videoUrls.split(",");
                String d480P = "", d720P = "", d1080P = "";
                for (String url : s) {
                    if (url.contains("480P:")) {
                        d480P = url.substring(5);
                    } else if (url.contains("720P:")) {
                        d720P = url.substring(5);
                    } else if (url.contains("1080P:")) {
                        d1080P = url.substring(6);
                    }
                }

                JSONObject jobj = new JSONObject();
                jobj.put("1080P", d1080P);
                jobj.put("720P", d720P);
                jobj.put("480P", d480P);
                articlePage.setVideoUrls(jobj.toString());

            }
            articlePage.setLikeNumber((Integer) conArticleService.selectLikeNumberById(articlePage.getId()).getData());
            articlePage.setCommentNumber((Integer) conArticleService.selectCommentNumberById(articlePage.getId()).getData());
        }
        Map<String, Object> map = IPages.buildDataMap(conArticlePage);
        return R.ok(map);
    }

    @Override
    public R<?> selectPersonalHomePageContent(Integer current, Integer size) {
        if (size == null || current == null) {
            current = 1;
            size = 10;
        }
        LambdaQueryWrapper<ConTopic> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select();
        List<TopicModule> result = new LinkedList<>();//存放最终结果
        Page<ConTopic> conTopicPage = new Page<>(current, size);//存放分页信息
        Page<ConTopic> topicPageList = conTopicMapper.selectPage(conTopicPage, lambdaQueryWrapper);//存放分页查询结果

        for (ConTopic conTopic : topicPageList.getRecords()) {
            List<ConArticle> articles = conTopicMapper.selectArticlesUnderCurrentTopic(conTopic.getTopicId());
            List<ArticlePagesOfTopicModule> articlePages = new LinkedList<>();
            for (int i = 0; i < articles.size(); i++) {
                ArticlePagesOfTopicModule articlePagesOfTopicModule = new ArticlePagesOfTopicModule();

                articlePagesOfTopicModule.setArticleId(articles.get(i).getId());
                articlePagesOfTopicModule.setCover(articles.get(i).getCoverUrl());
                articlePagesOfTopicModule.setTitle(articles.get(i).getTitle());
                articlePagesOfTopicModule.setReadNum(articles.get(i).getReadNumber());
                articlePages.add(articlePagesOfTopicModule);
            }

            TopicModule topicModule = new TopicModule();

            topicModule.setTopicId(conTopic.getTopicId());
            topicModule.setTitle(conTopic.getTitle());
            topicModule.setCoverUrl(conTopic.getCoverUrl());
            topicModule.setArticlePages(articlePages);
            if(articlePages==null||articlePages.isEmpty()){
                continue;
            }
            result.add(topicModule);
        }
        return R.ok(R.buildDataMap(result, conTopicPage.getTotal()));
    }

    @Override
    public R<?> selectUserOriginalContent(Integer current, Integer size, String userId) {
        Map<String, Object> column = conArticleMapper.selectNicknameAndProfileByUserId(userId);
        User user = new User();
        user.setUserId(userId);
        if (ProfileJudger.isNullProfile((String) column.get("photo_url"))) {
            user.setProfilePhotoUrl(null);
        } else {
            user.setProfilePhotoUrl((String) column.get("photo_url"));
        }
        ;
        user.setNickname((String) column.get("nickname"));


        Page<ArticlePage> articlePage = new Page<>(current, size);
        Page<ArticlePage> articlePages = conArticleMapper.selectPagingOriginalArticle(articlePage, userId);
        Map map = IPages.buildDataMap(articlePages);

        Map<String, Object> result = new HashMap<>();
        result.put("authorInfo", user);
        result.put("followNums", conFollowService.selectUserFollowNums(userId));
        result.put("followerNums", conFollowService.selectUserFollowerNums(userId));
        result.put("originalContent", map);
        result.put("isFollowThisAuthor", conFollowService.isFollowTheAuthor(userId));
        return R.ok(result);
    }

    @Override
    public R<?> selectHeaderAndPagingContent(Integer current, Integer size, String headerModuleType, String countyCode) {
        Map<String, Object> result = new HashMap<>();
        //1精选，2按照地区代码选择
        if ("1".equals(headerModuleType)) {
            List<Long> ids = new LinkedList<>();
            ids.add(1384080363344650242L);
            ids.add(1384080385020813314L);
            ids.add(1384080400405520386L);
            ids.add(1384330830612090882L);
            //data中存放 List<ConFunction>类型
            R msg = conFunctionService.selectGroupFunctionList(ids);
            result.put("header", msg.getData());
            result.put("PagingContent", selectPagingHomePageContent(current, size, "", "").getData());
            return R.ok(result);
        } else if ("2".equals(headerModuleType)) {
            result.put("PagingContent", selectPagingHomePageContent(current, size, "", countyCode).getData());
            return R.ok(result);
        } else {
            return R.ok();
        }
    }
}



