package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConComment;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConCommentMapper;
import com.dareway.jc.content.community.pojo.Commnet.CommentMsg;
import com.dareway.jc.content.community.pojo.Commnet.ReplyMsg;
import com.dareway.jc.content.community.pojo.User;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.ConArticleService;
import com.dareway.jc.content.community.service.ConCommentService;
import com.dareway.jc.content.community.service.ConLikeService;
import com.dareway.jc.content.community.utils.DateUtils;
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
public class ConCommentServiceImpl implements ConCommentService {
    @Autowired
    private ConCommentMapper conCommentMapper;

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private ConArticleService conArticleService;

    @Autowired
    private ConLikeService conLikeService;

    @Autowired
    private SecurityService securityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> createCommentToArticle(Long articleId, String content) {
        if ("".equals(content.trim())) {
            return R.fail("评论内容不能为空");
        }
        ConComment conComment = new ConComment();
        conComment.setArticleId(articleId);
        conComment.setContent(content);
        conComment.setPid(0L);
        conComment.setReadFlag("0");
        conComment.setCreator(securityService.getLoginUserId());
        conComment.setLikeNumber(0);
        conComment.setDeleteFlag("0");
        conComment.setCreateTime(new Date());
        Map<String, Object> column = new HashMap<>();
        //查询出当前评论所评论的文章
        column.put("id", conComment.getArticleId());
        List<ConArticle> articles = conArticleMapper.selectByMap(column);
        if ("1".equals(articles.get(0).getDeleteFlag())) {
            return R.fail("文章已被删除");
        }
        //将评论数加一添加回文章表
        articles.get(0).setCommentNumber(articles.get(0).getCommentNumber() + 1);
        conCommentMapper.insert(conComment);
        conArticleMapper.updateById(articles.get(0));

        User user = new User();
        Map<String, Object> nicknameAdnProfile = conArticleMapper.selectNicknameAndProfileByUserId(conComment.getCreator());
        user.setNickname((String) nicknameAdnProfile.get("nickname"));
        user.setProfilePhotoUrl((String) nicknameAdnProfile.get("photo_url"));
        user.setUserId(conComment.getCreator());
        CommentMsg commentMsg = new CommentMsg();
        commentMsg.setCreator(user);
        commentMsg.setId(conComment.getId());
        commentMsg.setCommentDate(conComment.getCreateTime());
        commentMsg.setContent(conComment.getContent());
        commentMsg.setLikeNumber(conComment.getLikeNumber());
        commentMsg.setLikeToComment(conLikeService.isLikeToComment(commentMsg.getId()));
        commentMsg.setFormatTime(DateUtils.fromToday(conComment.getCreateTime()));

        column.clear();
        column.put("commentNum",conArticleService.selectCommentNumberById(articleId).getData());
        column.put("commentMsg",commentMsg);

        return R.ok(column);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> createCommentAsReply(Long pid, String content) {
        ConComment conComment = new ConComment();
        conComment.setPid(pid);
        conComment.setContent(content);
        conComment.setReadFlag("0");
        conComment.setCreator(securityService.getLoginUserId());
        conComment.setLikeNumber(0);
        conComment.setDeleteFlag("0");
        conComment.setCreateTime(new Date());
        Map<String, Object> column = new HashMap<>();
        LambdaQueryWrapper<ConComment> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConComment::getId, conComment.getPid());
        ConComment parentComment = conCommentMapper.selectOne(lambdaQueryWrapper);
        if ("1".equals(parentComment.getDeleteFlag())) {
            return R.fail("评论已被删除");
        }
        //查询出当前评论所评论的文章
        column.put("id", parentComment.getArticleId());
        List<ConArticle> articles = conArticleMapper.selectByMap(column);
        conComment.setArticleId(parentComment.getArticleId());
        //将评论数加一添加回文章表
        articles.get(0).setCommentNumber(articles.get(0).getCommentNumber() + 1);
        conCommentMapper.insert(conComment);
        conArticleMapper.updateById(articles.get(0));

        lambdaQueryWrapper.clear();
        lambdaQueryWrapper.select().eq(ConComment::getPid,pid);
        int commentNum = conCommentMapper.selectCount(lambdaQueryWrapper);

        User creator = new User();
        User recipient = new User();
        Map<String, Object> nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(conComment.getCreator());
        creator.setNickname((String) nicknameAndProfile.get("nickname"));
        creator.setProfilePhotoUrl((String) nicknameAndProfile.get("photo_url"));
        creator.setUserId(conComment.getCreator());
        nicknameAndProfile.clear();


        //找到父评论的作者作为接受者
        nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(parentComment.getCreator());
        recipient.setNickname((String) nicknameAndProfile.get("nickname"));
        recipient.setProfilePhotoUrl((String) nicknameAndProfile.get("photo_url"));
        recipient.setUserId(conComment.getCreator());

        ReplyMsg replyMsg = new ReplyMsg();
        replyMsg.setPid(conComment.getPid());
        replyMsg.setId(conComment.getId());
        replyMsg.setCreator(creator);
        replyMsg.setRecipient(recipient);
        replyMsg.setReplyDate(conComment.getCreateTime());
        replyMsg.setContent(conComment.getContent());
        replyMsg.setLikeNumber(conComment.getLikeNumber());
        replyMsg.setIsLikeToComment(conLikeService.isLikeToComment(conComment.getId()));
        replyMsg.setFormatTime(DateUtils.fromToday(conComment.getCreateTime()));

        column.clear();
        column.put("commentNum",commentNum);
        column.put("relpyMsg",replyMsg);

        return R.ok(column);
    }

    @Override
    public R<Map<String, Object>> editComment(ConComment conComment) {
        conComment.setUpdater(securityService.getLoginUserId());
        conComment.setUpdateTime(new Date());
        conCommentMapper.updateById(conComment);
        return R.ok();
    }

    @Override
    public R<?> deleteComment(Long id) {
        ConComment conComment = new ConComment();
        conComment.setId(id);
        conComment.setUpdater(securityService.getLoginUserId());
        conComment.setUpdateTime(new Date());
        LambdaUpdateWrapper<ConComment> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper
                .set(ConComment::getDeleteFlag, 1)
                .eq(ConComment::getId, conComment.getId());
        return R.ok(conCommentMapper.update(null, lambdaUpdateWrapper));
    }

    @Override
    public R<Map<String, Object>> selectLikeNumberById(Long id) {
        Map<String, Object> column = new HashMap<>();
        LambdaQueryWrapper<ConComment> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(ConComment::getLikeNumber).eq(ConComment::getId, id);
        column.put("likeNumber", conCommentMapper.selectMaps(lambdaQueryWrapper));
        return R.ok(column);
    }

    @Override
    public R<?> selectCommentById(Long commentId, Integer current, Integer size) {
        Map<String, Object> result = new HashMap<>();
        ConComment conComment = conCommentMapper.selectById(commentId);
        List<ReplyMsg> replyMsgs = new LinkedList<>();
        conArticleService.selectChildrenReply(commentId, replyMsgs);
        List page = page(replyMsgs, size, current);
        result.put("result", page);
        result.put("total", replyMsgs.size());


        User user = new User();
        Map<String, Object> nicknameAdnProfile = conArticleMapper.selectNicknameAndProfileByUserId(conComment.getCreator());
        user.setNickname((String) nicknameAdnProfile.get("nickname"));
        user.setProfilePhotoUrl((String) nicknameAdnProfile.get("photo_url"));
        user.setUserId(conComment.getCreator());

        CommentMsg commentMsg = new CommentMsg();
        commentMsg.setCreator(user);
        commentMsg.setId(conComment.getId());
        commentMsg.setCommentDate(conComment.getCreateTime());
        commentMsg.setContent(conComment.getContent());
        commentMsg.setLikeNumber(conComment.getLikeNumber());
        commentMsg.setLikeToComment(conLikeService.isLikeToComment(commentMsg.getId()));
        commentMsg.setFormatTime(DateUtils.fromToday(conComment.getCreateTime()));

        result.put("comment", commentMsg);
        result.put("isLikeToComment", conLikeService.isLikeToComment(commentId));
        return R.ok(result);
    }

    public static List page(List dataList, int pageSize, int currentPage) {
        List currentPageList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                Object data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }
}
