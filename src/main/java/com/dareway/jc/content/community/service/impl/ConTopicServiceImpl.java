package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConArticleTopicRef;
import com.dareway.jc.content.community.domain.ConTopic;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConArticleTopicRefMapper;
import com.dareway.jc.content.community.mapper.ConTopicMapper;
import com.dareway.jc.content.community.pojo.Article.FuzzyQueryMsg;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.ConLikeService;
import com.dareway.jc.content.community.service.ConTopicService;
import com.dareway.jc.content.community.utils.DateUtils;
import com.dareway.jc.content.community.utils.IPages;
import com.dareway.jc.content.community.utils.ProfileJudger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lichp
 * @author WangPx
 * @since 2021-03-23
 * @since 2021-04-09
 */
@Service
public class ConTopicServiceImpl implements ConTopicService {
    @Autowired
    private ConTopicMapper conTopicMapper;

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private ConArticleTopicRefMapper conArticleTopicRefMapper;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ConLikeService conLikeService;

    @Override
    public R<Map<String, Object>> addTopic(String title, String des,String coverUrl) {
        ConTopic conTopic = new ConTopic();
        conTopic.setTitle(title);
        conTopic.setDes(des);
        conTopic.setSort(1);
        conTopic.setCreator(securityService.getLoginUserId());
        conTopic.setCreateTime(new Date());
        conTopic.setCoverUrl(coverUrl);
        conTopic.setDeleteFlag("0");
        conTopicMapper.insert(conTopic);
        return R.ok();
    }

    @Override
    public R<Map<String, Object>> editTopic(String title, String des, Integer sort, String coverUrl) {

        ConTopic conTopic = new ConTopic();

        conTopic.setTitle(title);
        conTopic.setDes(des);
        conTopic.setSort(sort);
        conTopic.setUpdator(securityService.getLoginUserId());
        conTopic.setUpdateTime(new Date());
        conTopic.setCoverUrl(coverUrl);
        conTopicMapper.updateById(conTopic);
        return R.ok();
    }

    @Override
    public R<Map<String, Object>> deleteTopic(Long topicId) {
        if (topicId == null) {
            return R.fail("topicId不能为空");
        }
        ConTopic conTopic = new ConTopic();
        conTopic.setTopicId(topicId);
        conTopic.setUpdateTime(new Date());
        conTopic.setUpdator(securityService.getLoginUserId());
        conTopic.setDeleteFlag("1");
        conTopicMapper.updateById(conTopic);
        return R.ok();
    }

    @Override
    public List selectAllTopic() {
        LambdaQueryWrapper<ConTopic> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConTopic::getDeleteFlag, "0");
        List<ConTopic> result = conTopicMapper.selectList(lambdaQueryWrapper);
        return result;
    }

    @Override
    public R<?> selectAllArticlesUnderCurrentTopic(Integer current, Integer size, Long topicId) {
        if (topicId == null) {
            return R.fail("topicId不能为空");
        }
        ConTopic conTopic = conTopicMapper.selectById(topicId);
        if ("1".equals(conTopic.getDeleteFlag())) {
            return R.fail("话题已删除");
        }

        Page<ConArticle> articlePage = new Page<>(current,size);
        Page<ConArticle> result = conTopicMapper.selectPagingAllArticlesUnderCurrentTopic(articlePage,topicId);
        Page<FuzzyQueryMsg> formatResult = new Page<>(current,size,result.getTotal());
        List<FuzzyQueryMsg> list = new LinkedList<>();
        for (ConArticle conArticle : result.getRecords()) {

            if(conArticle==null){
                continue;
            }
                FuzzyQueryMsg fuzzyQueryMsg = new FuzzyQueryMsg();
                fuzzyQueryMsg.setTitle(conArticle.getTitle());
                fuzzyQueryMsg.setCoverUrl(conArticle.getCoverUrl());
                fuzzyQueryMsg.setFormatTime(DateUtils.fromToday(conArticle.getCreateDate()));
                fuzzyQueryMsg.setId(conArticle.getId());
                fuzzyQueryMsg.setType(conArticle.getType());
                fuzzyQueryMsg.setCommentNumber(conArticle.getCommentNumber());
                fuzzyQueryMsg.setReadNumber(conArticle.getReadNumber());
                Map<String, Object> nicknameAdnProfile = conArticleMapper.selectNicknameAndProfileByUserId(conArticle.getCreator());
                fuzzyQueryMsg.setNickname((String) nicknameAdnProfile.get("nickname"));
                if(ProfileJudger.isNullProfile((String)nicknameAdnProfile.get("photo_url"))){
                    fuzzyQueryMsg.setPhotoUrl(null);
                }else {
                    fuzzyQueryMsg.setPhotoUrl((String) nicknameAdnProfile.get("photo_url"));
                }
                fuzzyQueryMsg.setUserId(conArticle.getCreator());
                fuzzyQueryMsg.setIsLike(conLikeService.isLikeToArticle(conArticle.getId()));
                fuzzyQueryMsg.setLikeNumber(conArticle.getLikeNumber());
                if("3".equals(conArticle.getType())){
                    fuzzyQueryMsg.setContent(conArticle.getContent());
                }
                list.add(fuzzyQueryMsg);
        }
        formatResult.setRecords(list);

        return R.ok(IPages.buildDataMap(formatResult));
    }

    @Override
    public R<Map<String, Object>> migrateTopic() {
        LambdaQueryWrapper<ConTopic> queryWrapper = Wrappers.lambdaQuery();
        List<ConTopic> conTopics = conTopicMapper.selectList(queryWrapper);
        for (ConTopic conTopic : conTopics) {
            ConArticle conArticle = conArticleMapper.selectById(conTopic.getTopicId());
            if (conArticle == null) {
                LambdaUpdateWrapper<ConArticleTopicRef> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
                lambdaUpdateWrapper.eq(ConArticleTopicRef::getTopicId, conTopic.getTopicId());
                conArticleTopicRefMapper.delete(lambdaUpdateWrapper);
            }
        }

        LambdaQueryWrapper<ConArticle> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select();
        List<ConArticle> conArticles = conArticleMapper.selectList(lambdaQueryWrapper);
        for (ConArticle conArticle : conArticles) {
            LambdaUpdateWrapper<ConArticleTopicRef> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
            lambdaUpdateWrapper.eq(ConArticleTopicRef::getArticleId, conArticle.getId());
            conArticleTopicRefMapper.delete(lambdaUpdateWrapper);

            ConArticleTopicRef conArticleTopicRef = new ConArticleTopicRef();
            conArticleTopicRef.setArticleId(conArticle.getId());
            //conArticleTopicRef.setTopicId(conArticle.getTopicId());
            conArticleTopicRef.setCreateTime(new Date());
            conArticleTopicRefMapper.insert(conArticleTopicRef);
        }

        return R.ok();
    }

    @Override
    public R<?> countCurrentTopicReadNumber(Long topicId) {
        Integer topicReadNumber = conTopicMapper.selectAllArticlesUnderCurrentTopic(topicId);

//        for (ConArticle conArticle : conArticles) {
//            topicReadNumber+= conArticle.getReadNumber();
//        }
        Map<String,Object> column = new HashMap<>();
        column.put("topicReadNumber",topicReadNumber);
        return R.ok(column);
    }


}
