package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConComment;
import com.dareway.jc.content.community.domain.ConLike;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConCommentMapper;
import com.dareway.jc.content.community.mapper.ConLikeMapper;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.ConLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ConLikeServiceImpl implements ConLikeService {

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private ConLikeMapper conLikeMapper;

    @Autowired
    private ConCommentMapper conCommentMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public boolean isLikeToComment(Long commentId) {
        LambdaQueryWrapper<ConLike> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConLike::getCreator, securityService.getLoginUserId());
        //查询当前用户所有点赞记录
        List<ConLike> curUserLikeCommentList = conLikeMapper.selectList(lambdaQueryWrapper);

        for (ConLike conLike : curUserLikeCommentList) {
            if (commentId.equals(conLike.getCommentId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLikeToArticle(Long articleId) {
        LambdaQueryWrapper<ConLike> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConLike::getCreator, securityService.getLoginUserId());
        //查询当前用户所有点赞记录
        List<ConLike> curUserLikeCommentList = conLikeMapper.selectList(lambdaQueryWrapper);

        for (ConLike conLike : curUserLikeCommentList) {
            if (articleId.equals(conLike.getArticleId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> likeToArticle(Long articleId) {
        if (isLikeToArticle(articleId)) {
            return R.fail("不可重复点赞");
        }
        ConLike conLike = new ConLike();
        conLike.setReadFlag("0");
        conLike.setCreateTime(new Date());
        conLike.setCreator(securityService.getLoginUserId());
        conLike.setArticleId(articleId);
        conLikeMapper.insert(conLike);
        //查询出点赞的文章
        ConArticle articles = conArticleMapper.selectById(articleId);
        //将点赞数加一添加回文章表
        if ("1".equals(articles.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        articles.setLikeNumber(articles.getLikeNumber() + 1);
        conArticleMapper.updateById(articles);
        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> likeToComment(Long commentId) {
        if (isLikeToComment(commentId)) {
            return R.fail("不可重复点赞");
        }
        ConLike conLike = new ConLike();

        Map<String, Object> column = new HashMap<>();
        //查询出点赞的评论
        column.put("id", commentId);//column存放文章id Map(id:articleID)
        List<ConComment> comments = conCommentMapper.selectByMap(column);

        long articleId = comments.get(0).getArticleId();//获取当前评论对应的文章id
        conLike.setArticleId(articleId);
        if ("1".equals(comments.get(0).getDeleteFlag())) {
            return R.fail("评论已删除");
        }
        //将点赞数加一添加回评论表
        comments.get(0).setLikeNumber(comments.get(0).getLikeNumber() + 1);
        conCommentMapper.updateById(comments.get(0));
        //初始化点赞信息后放入点赞记录表用作通知未读消息使用
        conLike.setCommentId(commentId);
        conLike.setReadFlag("0");
        conLike.setCreateTime(new Date());
        conLike.setCreator(securityService.getLoginUserId());
        conLikeMapper.insert(conLike);

        return R.ok();
    }
}
