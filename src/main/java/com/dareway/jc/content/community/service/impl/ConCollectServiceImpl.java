package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConCollect;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConCollectMapper;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.ConArticleService;
import com.dareway.jc.content.community.service.ConCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.event.ObjectChangeListener;
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
public class ConCollectServiceImpl implements ConCollectService {
    @Autowired
    private ConCollectMapper conCollectMapper;
    @Autowired
    private ConArticleMapper conArticleMapper;
    @Autowired
    private SecurityService securityService;


    @Override
    public R<?> addToCollection(Long articleId) {
        LambdaQueryWrapper<ConCollect> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConCollect::getArticleId, articleId).eq(ConCollect::getCreator, securityService.getLoginUserId());
        ConCollect conCollect = conCollectMapper.selectOne(lambdaQueryWrapper);

        if (conCollect != null) {
            LambdaUpdateWrapper<ConCollect> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
            if ("1".equals(conCollect.getDeleteFlag())) {
                lambdaUpdateWrapper.set(ConCollect::getDeleteFlag, "0")
                        .eq(ConCollect::getArticleId, articleId);
                conCollectMapper.update(null, lambdaUpdateWrapper);
                return R.ok("已收藏");
            } else {
                lambdaUpdateWrapper.set(ConCollect::getDeleteFlag, "1")
                        .eq(ConCollect::getArticleId, articleId);
                conCollectMapper.update(null, lambdaUpdateWrapper);
                return R.ok("已取消收藏");
            }

        }

        if (conCollect == null) {
            conCollect = new ConCollect();
        }
        conCollect.setArticleId(articleId);
        conCollect.setCreateTime(new Date());
        conCollect.setCreator(securityService.getLoginUserId());
        conCollect.setDeleteFlag("0");
        conCollectMapper.insert(conCollect);
        return R.ok("已收藏");
    }

    @Override
    public R<?> selectAllCollection(String userId) {
        List<ConCollect> conCollects;
        LambdaQueryWrapper<ConCollect> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConCollect::getCreator, userId).eq(ConCollect::getDeleteFlag, "0");
        conCollects = conCollectMapper.selectList(lambdaQueryWrapper);
        List<ConArticle> resultList = new LinkedList<>();
        for (ConCollect conCollect : conCollects) {
            Long articleId = conCollect.getArticleId();
            ConArticle conArticle = conArticleMapper.selectById(articleId);
            resultList.add(conArticle);
        }
        return R.ok(resultList);
    }

    @Override
    public boolean isCollectArticle(Long articleId) {
        LambdaQueryWrapper<ConCollect> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConCollect::getCreator, securityService.getLoginUserId())
                .eq(ConCollect::getArticleId, articleId);
        List<ConCollect> conCollect = conCollectMapper.selectList(lambdaQueryWrapper);
        if (conCollect.size() == 0) {
            return false;
        } else {
            if ("1".equals(conCollect.get(0).getDeleteFlag())) {
                return false;
            } else {
                return true;
            }
        }
    }

}
