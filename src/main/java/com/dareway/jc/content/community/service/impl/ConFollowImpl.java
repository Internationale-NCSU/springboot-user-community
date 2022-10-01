package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dareway.jc.content.community.domain.ConFollow;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConCommentMapper;
import com.dareway.jc.content.community.mapper.ConFollowMapper;
import com.dareway.jc.content.community.pojo.User;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.ConFollowService;
import com.dareway.jc.content.community.utils.ProfileJudger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class ConFollowImpl implements ConFollowService {
    @Autowired
    private ConFollowMapper conFollowMapper;

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public R<?> followOrUnfollowTheAuthor(String id) {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConFollow::getUserId, id)
                .eq(ConFollow::getFollowerId, securityService.getLoginUserId());

        ConFollow conFollow = conFollowMapper.selectOne(lambdaQueryWrapper);
        if (conFollow == null) {
            conFollow = new ConFollow();
            conFollow.setFollowerId(securityService.getLoginUserId());
            conFollow.setUserId(id);
            conFollow.setCreateTime(new Date());
            conFollowMapper.insert(conFollow);
            return R.ok("已关注");
        } else {
            conFollowMapper.deleteById(conFollow.getId());
            return R.ok("已取消关注");
        }
    }

    @Override
    public R<?> selectUserFollowList() {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConFollow::getFollowerId, securityService.getLoginUserId());
        List<ConFollow> conFollows = conFollowMapper.selectList(lambdaQueryWrapper);
        List<Map<String, Object>> result = new LinkedList<>();
        for (ConFollow c : conFollows) {
            User user = new User();
            Map<String, Object> column = conArticleMapper.selectNicknameAndProfileByUserId(c.getUserId());

            user.setUserId(c.getUserId());
            if(ProfileJudger.isNullProfile((String)column.get("photo_url"))){
                user.setProfilePhotoUrl(null);
            }else {
                user.setProfilePhotoUrl((String) column.get("photo_url"));
            }
            user.setNickname((String) column.get("nickname"));

            column.clear();
            column.put("user", user);
            column.put("followerNum", selectUserFollowerNums(c.getUserId()));
            column.put("userTotalLikeNumbers", selectUserTotalLikeNums(c.getUserId()));
            result.add(column);
        }
        return R.ok(result);
    }

    @Override
    public R<?> selectUserFollowerList() {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConFollow::getUserId, securityService.getLoginUserId());
        List<ConFollow> conFollows = conFollowMapper.selectList(lambdaQueryWrapper);
        List<Map<String, Object>> result = new LinkedList<>();
        for (ConFollow c : conFollows) {
            User user = new User();
            Map<String, Object> column = conArticleMapper.selectNicknameAndProfileByUserId(c.getUserId());
            user.setUserId(c.getUserId());
            user.setNickname((String) column.get("nickname"));
            if(ProfileJudger.isNullProfile((String)column.get("photo_url"))){
                user.setProfilePhotoUrl(null);
            }else {
                user.setProfilePhotoUrl((String) column.get("photo_url"));
            }
            column.clear();
            column.put("user", user);
            column.put("followerNum", selectUserFollowerNums(c.getUserId()));
            column.put("userTotalLikeNumbers", selectUserTotalLikeNums(c.getUserId()));
            result.add(column);
        }
        return R.ok(result);
    }

    @Override
    public int selectUserFollowerNums(String userId) {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        //他是被关注者，统计其粉丝数
        lambdaQueryWrapper.select().eq(ConFollow::getUserId, userId);
        int num = conFollowMapper.selectCount(lambdaQueryWrapper);
        return num;
    }

    @Override
    public int selectUserFollowNums(String userId) {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        //他是关注者，统计其关注的作者数
        lambdaQueryWrapper.select().eq(ConFollow::getFollowerId, userId);
        int num = conFollowMapper.selectCount(lambdaQueryWrapper);
        return num;
    }

    @Override
    public int selectUserTotalLikeNums(String userId) {
        Integer commentNumbers = conFollowMapper.selectUserCommentTotalLikeNums(userId);
        Integer articleNumbers = conFollowMapper.selectUserArticleTotalLikeNums(userId);
        if (commentNumbers == null) {
            commentNumbers = 0;
        }
        if (articleNumbers == null) {
            articleNumbers = 0;
        }
        return commentNumbers + articleNumbers;
    }

    @Override
    public boolean isFollowTheAuthor(String authorUserId) {
        LambdaQueryWrapper<ConFollow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConFollow::getUserId, authorUserId)
                .eq(ConFollow::getFollowerId, securityService.getLoginUserId());
        List<ConFollow> conFollows = conFollowMapper.selectList(lambdaQueryWrapper);
        if (conFollows == null || conFollows.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
