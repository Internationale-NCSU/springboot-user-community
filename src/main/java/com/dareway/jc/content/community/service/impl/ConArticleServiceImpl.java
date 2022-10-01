package com.dareway.jc.content.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.ConArticleTopicRef;
import com.dareway.jc.content.community.domain.ConComment;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.feign.FileFeignService;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.mapper.ConArticleTopicRefMapper;
import com.dareway.jc.content.community.mapper.ConCommentMapper;
import com.dareway.jc.content.community.pojo.Article.FuzzyQueryMsg;
import com.dareway.jc.content.community.pojo.Article.OldArticle;
import com.dareway.jc.content.community.pojo.Commnet.CommentMsg;
import com.dareway.jc.content.community.pojo.Commnet.ReplyMsg;
import com.dareway.jc.content.community.pojo.Topic.TitlesAndTopicId;
import com.dareway.jc.content.community.pojo.User;
import com.dareway.jc.content.community.security.SecurityService;
import com.dareway.jc.content.community.service.*;
import com.dareway.jc.content.community.utils.*;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
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
public class ConArticleServiceImpl implements ConArticleService {

    @Autowired
    private FileFeignService fileFeignService;

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private ConCommentMapper conCommentMapper;

    @Autowired
    private ConLikeService conLikeService;

    @Autowired
    private ConCollectService conCollectService;

    @Autowired
    private ConArticleTopicRefMapper conArticleTopicRefMapper;

    @Autowired
    private VideoTranscodingService videoTranscodingService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ConFollowService conFollowService;

    private String migrationTestUserId = "2019101500000000000001118891";

    private String currentServerIp = "http://10.30.0.95:8080";

    private String getCurrentServerIp() {
        return currentServerIp;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public R<Map<String, Object>> createArticle(Long articleId, String type, String title,
                                                String subtitle, String content, String videoUrl,
                                                List<Long> topicIds, String draftStatus, String coverUrl, String countyCode, String isWebUrl) {
        //空值处理(subtitle可以为空，subtitle为空直接用title填)
        if (articleId == null) {
            if ((!"1".equals(type)) && (!"2".equals(type))&& (!"3".equals(type)) && (!"4".equals(type))) {
                return R.fail("请返回正确的内容类型");
            }
            if (title == null || "".equals(title.trim())) {
                if("1".equals(type)||"2".equals(type)) {
                    return R.fail("文章标题不能为空");
                }else if("3".equals(type)){
                    return R.fail("帖子内容不能为空");
                }
            }
            if (content == null || "".equals(content.trim())) {
                if("1".equals(type)||"2".equals(type)) {
                    return R.fail("文章内容不能为空");
                }else if("3".equals(type)){
                    return R.fail("请选择图片");
                }
            }
            if (subtitle == null || "".equals(content.trim())) {
                subtitle = title;
            }

            //准备一个异步方法执行视频转码


            ConArticle conArticle = new ConArticle();

            conArticle.setCreator(securityService.getLoginUserId());
            conArticle.setLikeNumber(0);
            conArticle.setCommentNumber(0);
            conArticle.setDeleteFlag("0");
            conArticle.setCreateDate(new Date());
            conArticle.setReadNumber(0);
            conArticle.setTitle(title);
            conArticle.setSubTitle(subtitle);
            conArticle.setContent(content);
            conArticle.setVideoUrl(videoUrl);
            conArticle.setCountyCode(countyCode);
            conArticle.setIsWebUrl(isWebUrl);
            conArticle.setDraftStatus(draftStatus);
            conArticle.setType(type);
            conArticle.setCoverUrl(coverUrl);
            conArticleMapper.insert(conArticle);
            if(draftStatus!=null) {
                if ("0".equals(draftStatus)) {
                    LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
                    lambdaUpdateWrapper.eq(ConArticle::getDraftStatus, "1")
                            .eq(ConArticle::getCreator, securityService.getLoginUserId());
                    conArticleMapper.delete(lambdaUpdateWrapper);
                }
            }

            if(("1".equals(type)||"2".equals(type))&&"0".equals(conArticle.getDraftStatus())&&!videoUrl.isEmpty()){
                try {
                    JSONObject jsonObject = JSON.parseObject(videoUrl);
                    String trueUrl = jsonObject.getString("1080P");
                    System.out.println("realUrl: " + trueUrl);
                    videoTranscodingService.videoTranscoding480(trueUrl,conArticle.getId());
                    videoTranscodingService.videoTranscoding720(trueUrl,conArticle.getId());
                    videoTranscodingService.videoTranscoding1080(trueUrl,conArticle.getId());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }


            //遍历多话题添加到文章话题关联(ConArticleTopicRef)记录表
            for (Long id : topicIds) {
                ConArticleTopicRef conArticleTopicRef = new ConArticleTopicRef();
                conArticleTopicRef.setCreateTime(new Date());
                conArticleTopicRef.setArticleId(conArticle.getId());
                conArticleTopicRef.setTopicId(id);
                conArticleTopicRefMapper.insert(conArticleTopicRef);
            }

            return R.ok();
        } else {
            return editArticle(articleId, title, subtitle, content, topicIds, draftStatus,countyCode);
        }
    }

    @Override
    public R<Map<String, Object>> editArticle(Long articleId, String title, String subtitle,
                                              String content, List<Long> topicIds, String draftStatus,String countyCode) {

        ConArticle conArticle = conArticleMapper.selectById(articleId);
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        conArticle.setUpdater(securityService.getLoginUserId());
        conArticle.setUpdateTime(new Date());
        conArticle.setTitle(title);
        conArticle.setSubTitle(subtitle);
        conArticle.setContent(content);
        conArticle.setCountyCode(countyCode);
        conArticle.setDraftStatus(draftStatus);
        LambdaUpdateWrapper<ConArticleTopicRef> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(ConArticleTopicRef::getArticleId,articleId);
        //先从话题表中删除当前文章原有的所有话题记录
        conArticleTopicRefMapper.delete(lambdaUpdateWrapper);
        //话题表中添加编辑后的话题记录
        for (Long id : topicIds) {
            ConArticleTopicRef conArticleTopicRef = new ConArticleTopicRef();
            conArticleTopicRef.setCreateTime(new Date());
            conArticleTopicRef.setArticleId(conArticle.getId());
            conArticleTopicRef.setTopicId(id);
            conArticleTopicRefMapper.insert(conArticleTopicRef);
        }
        conArticleMapper.updateById(conArticle);
        return R.ok();
    }

    @Override
    public R<?> deleteArticle(Long articleId) {
        ConArticle conArticle = conArticleMapper.selectById(articleId);
        conArticle.setUpdater(securityService.getLoginUserId());
        conArticle.setUpdateTime(new Date());
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章不存在");
        }
        //删除操作即将delete_flag字段置“1”
        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper
                .set(ConArticle::getDeleteFlag, "1")
                .set(ConArticle::getUpdater, conArticle.getUpdater())
                .set(ConArticle::getUpdateTime, conArticle.getUpdateTime())
                .eq(ConArticle::getId, conArticle.getId());

        conArticleMapper.update(null, lambdaUpdateWrapper);
        if ("1".equals(conArticle.getDraftStatus())) {
            return R.ok("草稿已删除");
        } else {
            return R.ok("文章已删除");
        }
    }

    @Override
    public R<?> deleteDraft(Long articleId) {
        ConArticle conArticle = conArticleMapper.selectById(articleId);
        if("0".equals(conArticle.getDraftStatus())){
            return R.fail("不可调用此方法删除已发布的文章");
        }
        conArticleMapper.deleteById(articleId);
        return R.ok();
    }

    @Override
    public R<?> selectLikeNumberById(Long id) {
        ConArticle conArticle = conArticleMapper.selectById(id);
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        return R.ok(conArticle.getLikeNumber());
    }

    @Override
    public R<?> selectCommentNumberById(Long id) {
        ConArticle conArticle = conArticleMapper.selectById(id);
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        return R.ok(conArticle.getCommentNumber());
    }

    /**
     * <p>
     * 请求文章内容 与 评论
     * </p>
     *
     * @author PinxiangWang
     * @since 2021-03-24
     */
    @Override
    public R<?> selectArticleContent(Long articleId) {
        if (articleId == null) {
            return R.fail("请上传文章id");
        }

        Map<String, Object> column = new HashMap<>();
        Map<String, Object> nicknameAndProfile;
        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        ConArticle conArticle = conArticleMapper.selectById(articleId);
        if (conArticle == null) {
            return R.fail("未查询到此文章");
        }
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }

        //每执行一次此方法，文章阅读量+1
        lambdaUpdateWrapper
                .set(ConArticle::getReadNumber, conArticle.getReadNumber() + 1)
                .eq(ConArticle::getId, conArticle.getId());

        //NicknameAndProfile 用以封装用户名与用户头像链接
        nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(conArticle.getCreator());
        conArticleMapper.update(null, lambdaUpdateWrapper);
        //组装 文章内容 结果集
        column.put("authorNickname", nicknameAndProfile.get("nickname"));

        if(ProfileJudger.isNullProfile((String)nicknameAndProfile.get("photo_url"))){
            column.put("authorProfileUrl",null);
        }else {
            column.put("authorProfileUrl", nicknameAndProfile.get("photo_url"));
        }
        column.put("authorId", conArticle.getCreator());
        column.put("subTitle", conArticle.getSubTitle());
        column.put("readNumbers", conArticle.getReadNumber());
        column.put("createDate", sdf.format(conArticle.getCreateDate()));
        column.put("content", conArticle.getContent());
        column.put("type", conArticle.getType());
        column.put("isWebUrl",conArticle.getIsWebUrl());
        column.put("videoUrls",conArticle.getVideoUrls());
        column.put("formatTime",DateUtils.fromToday(conArticle.getCreateDate()));
        column.put("likeNumber", conArticle.getLikeNumber());
        column.put("commentNumber", conArticle.getCommentNumber());
        column.put("isLikeToArticle", conLikeService.isLikeToArticle(articleId));
        column.put("isCollectArticle", conCollectService.isCollectArticle(articleId));
        column.put("isFollowThisAuthor", conFollowService.isFollowTheAuthor(conArticle.getCreator()));
        if(("1".equals(conArticle.getType())||"2".equals(conArticle.getType()))&&conArticle.getVideoUrls()!=null) {
            String videoUrls = conArticle.getVideoUrls();
            String[] s = videoUrls.split(",");
            String d480P="",d720P="",d1080P="";
            for (String url : s) {
                if (url.contains("480P:")) {
                    d480P = url.substring(5);
                } else if (url.contains("720P:")) {
                    d720P = url.substring(5);
                } else if (url.contains("1080P:")) {
                    d1080P = url.substring(6);
                }
            }

            JSONObject jobj=new JSONObject();
            jobj.put("1080P",d1080P);
            jobj.put("720P",d720P);
            jobj.put("480P",d480P);
            conArticle.setVideoUrls(jobj.toString());

        }

        return R.ok(column);
    }

    @Override
    public R<?> selectArticleByTitle(String title) {
        LambdaQueryWrapper<ConArticle> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                //未被删除
                .eq(ConArticle::getDeleteFlag, "0")
                //不是草稿
                .eq(ConArticle::getDraftStatus,"0")
                .like(ConArticle::getTitle, title)
        .orderByDesc(ConArticle::getType);
        List<ConArticle> articles = conArticleMapper.selectList(lambdaQueryWrapper);
        List<FuzzyQueryMsg> result = new LinkedList<>();
        for (ConArticle conArticle: articles) {
            FuzzyQueryMsg fuzzyQueryMsg = new FuzzyQueryMsg();
            fuzzyQueryMsg.setTitle(conArticle.getTitle());
            fuzzyQueryMsg.setModifiedTitle(highLight(conArticle.getTitle(),title));
            fuzzyQueryMsg.setCoverUrl(conArticle.getCoverUrl());
            fuzzyQueryMsg.setFormatTime(DateUtils.fromToday(conArticle.getCreateDate()));
            fuzzyQueryMsg.setId(conArticle.getId());
            fuzzyQueryMsg.setType(conArticle.getType());
            fuzzyQueryMsg.setCommentNumber(conArticle.getCommentNumber());
            fuzzyQueryMsg.setReadNumber(conArticle.getReadNumber());
            Map<String, Object> nicknameAdnProfile = conArticleMapper.selectNicknameAndProfileByUserId(conArticle.getCreator());

            if(ProfileJudger.isNullProfile((String)nicknameAdnProfile.get("photo_url"))){
                fuzzyQueryMsg.setPhotoUrl(null);
            }else {
                fuzzyQueryMsg.setPhotoUrl((String) nicknameAdnProfile.get("photo_url"));
            }fuzzyQueryMsg.setNickname((String) nicknameAdnProfile.get("nickname"));

            fuzzyQueryMsg.setUserId(conArticle.getCreator());
            fuzzyQueryMsg.setIsLike(conLikeService.isLikeToArticle(conArticle.getId()));
            fuzzyQueryMsg.setLikeNumber(conArticle.getLikeNumber());
            if("3".equals(conArticle.getType())){
                fuzzyQueryMsg.setContent(conArticle.getContent());
            }
            result.add(fuzzyQueryMsg);
        }


        return R.ok(result);
    }

    public String highLight(String s,String keyWord){
        int startIndex = s.indexOf(keyWord);
        StringBuffer stringBuffer = new StringBuffer();
        if(startIndex==0){
            stringBuffer
                    .append("<span style=\"color:#F00\">")
                    .append(keyWord)
                    .append("</span>")
                    .append(s.substring(keyWord.length()));
        }else if(startIndex+keyWord.length()!=s.length()) {
            stringBuffer
                    .append(s, 0, startIndex)
                    .append("<span style=\"color:#F00\">")
                    .append(keyWord)
                    .append("</span>")
                    .append(s.substring(startIndex+keyWord.length()+1));
        }else {
            stringBuffer
                    .append(s, 0, startIndex)
                    .append("<span style=\"color:#F00\">")
                    .append(keyWord)
                    .append("</span>");
        }
        return stringBuffer.toString();
    };

    @Override
    public R<?> selectAllTopicByArticleId(Long id) {
        ConArticle conArticle = conArticleMapper.selectById(id);
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        List<TitlesAndTopicId> titlesAndTopicIds = conArticleMapper.selectTopicIdsAndTitles(conArticle.getId());
        return R.ok(titlesAndTopicIds);
    }

    @Override
    public R<?> selectDraftByType(String type) {
        LambdaQueryWrapper<ConArticle> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConArticle::getType, type)
                .eq(ConArticle::getDraftStatus, "1")
                .eq(ConArticle::getDeleteFlag, "0")
                .eq(ConArticle::getCreator, securityService.getLoginUserId())
                .orderByDesc(ConArticle::getCreateDate)
                .last("limit 1");
        ConArticle conArticle = conArticleMapper.selectOne(lambdaQueryWrapper);

        if (conArticle == null) {
            return R.fail("未查询到草稿");
        } else {
            List<TitlesAndTopicId> titlesAndTopicIds = conArticleMapper.selectTopicIdsAndTitles(conArticle.getId());
            Map<String, Object> result = new HashMap<>();
            result.put("article", conArticle);
            result.put("titlesAndTopicIds", titlesAndTopicIds);
            return R.ok(result);
        }
    }

    @Override
    public R<?> imageUpload() {
        String fileUrl = "null";
        try {
            File file = new File("C:\\Users\\PxWang\\Desktop\\VideoTest\\eb0d3d5e8e544db8bf0610d6bda687d3压缩720.mp4");
            FileItem fileItem = VideoClipUtil.createFileItem(file);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            fileUrl = (fileFeignService.uploadByFile(multipartFile,"/user/java/unicronaged/dev/content/article/")).getData().getUrl();
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok(fileUrl);
    }

    @Override
    public R<?> selectArticleAllComments(Long articleId, Integer current, Integer size)  {
        if (articleId == null) {
            return R.fail("请上传文章id");
        }
        if (size == null || current == null) {
            current = 1;
            size = 10;
        }
        Page<ConComment> page = new Page<>(current, size);

        ConArticle conArticle = conArticleMapper.selectById(articleId);
        if (conArticle == null) {
            return R.fail("未查询到此文章");
        }
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }

        List<CommentMsg> commentMsgs = new LinkedList<>();

        LambdaQueryWrapper<ConComment> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConComment::getArticleId, articleId)
                .eq(ConComment::getPid, 0L)
                .eq(ConComment::getDeleteFlag, "0")//只查没被删除的评论
                .orderBy(true, false, ConComment::getCreateTime);

        //查询这篇文章的全部顶层评论
        Page<ConComment> commentPage = conCommentMapper.selectPage(page, lambdaQueryWrapper);

        //将commentPage评论整合进入CommentMsg
        for (ConComment conComment : commentPage.getRecords()) {
            List<ReplyMsg> children = new LinkedList<>();
            User user = new User();
            Map<String, Object> nicknameAdnProfile = conArticleMapper.selectNicknameAndProfileByUserId(conComment.getCreator());

            if(ProfileJudger.isNullProfile((String)nicknameAdnProfile.get("photo_url"))){
                user.setProfilePhotoUrl(null);
            }else {
                user.setProfilePhotoUrl((String) nicknameAdnProfile.get("photo_url"));
            }

            user.setNickname((String) nicknameAdnProfile.get("nickname"));

            user.setUserId(conComment.getCreator());
            selectChildrenReply(conComment.getId(), children);

            Collections.sort(children);

            CommentMsg commentMsg = new CommentMsg();
            commentMsg.setCreator(user);
            commentMsg.setId(conComment.getId());
            commentMsg.setCommentDate(conComment.getCreateTime());
            commentMsg.setContent(conComment.getContent());
            commentMsg.setLikeNumber(conComment.getLikeNumber());
            commentMsg.setChildren(children);
            commentMsg.setLikeToComment(conLikeService.isLikeToComment(commentMsg.getId()));
            commentMsg.setFormatTime(DateUtils.fromToday(conComment.getCreateTime()));
            commentMsgs.add(commentMsg);
        }
        return R.ok(R.buildDataMap(commentMsgs, commentPage.getTotal()));
    }

    //递归实现查询子评论（回复）
    @Override
    public void selectChildrenReply(Long id, List<ReplyMsg> children) {
        LambdaQueryWrapper<ConComment> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select()
                .eq(ConComment::getPid, id);
        //查询ConComment表中pid = id的子评论字段
        List<ConComment> conComments = conCommentMapper.selectList(lambdaQueryWrapper);
        if (!conComments.isEmpty()) {
            //将子评论封装成ReplyMsg并放入结果集List<ReplyMsg> children.
            for (ConComment conComment : conComments) {
                User creator = new User();
                User recipient = new User();
                Map<String, Object> nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(conComment.getCreator());
                if(ProfileJudger.isNullProfile((String)nicknameAndProfile.get("photo_url"))){
                    creator.setProfilePhotoUrl(null);
                }else {
                   creator.setProfilePhotoUrl((String) nicknameAndProfile.get("photo_url"));
                }

                creator.setNickname((String) nicknameAndProfile.get("nickname"));
                creator.setUserId(conComment.getCreator());
                nicknameAndProfile.clear();

                //这条评论所回复的那条父评论
                ConComment parentComment = conCommentMapper.selectById(conComment.getPid());

                //找到父评论的作者作为接受者
                nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(parentComment.getCreator());
                recipient.setNickname((String) nicknameAndProfile.get("nickname"));
                if(ProfileJudger.isNullProfile((String)nicknameAndProfile.get("photo_url"))){
                    recipient.setProfilePhotoUrl(null);
                }else {
                    recipient.setProfilePhotoUrl((String) nicknameAndProfile.get("photo_url"));
                }

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
                
                //只加没被删除的子评论（回复）
                if ("0".equals(conComment.getDeleteFlag())) {
                    children.add(replyMsg);
                }

                selectChildrenReply(conComment.getId(), children);
            }
        }

    }

    @Override
    public R<Map<String, Object>> selectArticleTitleById(Long id) {

        LambdaQueryWrapper<ConArticle> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(ConArticle::getId, id);
        ConArticle conArticle = conArticleMapper.selectOne(lambdaQueryWrapper);
        if (conArticle == null) {
            return R.fail("未查询到此文章");
        }
        if ("1".equals(conArticle.getDeleteFlag())) {
            return R.fail("文章已删除");
        }
        Map<String, Object> nicknameAndProfile = conArticleMapper.selectNicknameAndProfileByUserId(conArticle.getCreator());
        User user = new User();

        if(ProfileJudger.isNullProfile((String)nicknameAndProfile.get("photo_url"))){
            user.setProfilePhotoUrl(null);
        }else {
            user.setProfilePhotoUrl((String) nicknameAndProfile.get("photo_url"));
        }
        user.setNickname((String) nicknameAndProfile.get("nickname"));
        Map<String, Object> column = new HashMap<>();

        List<Long> topicIds = new LinkedList<>();
        List<String> topicTitles = new LinkedList<>();
        List<TitlesAndTopicId> titlesAndTopicIds = conArticleMapper.selectTopicIdsAndTitles(id);
        if (!titlesAndTopicIds.isEmpty()) {
            for (TitlesAndTopicId t : titlesAndTopicIds) {
                if (t != null) {
                    topicIds.add(t.getTopicId());
                    topicTitles.add(t.getTitle());
                }
            }
        }

        column.put("Title", conArticle.getTitle());
        column.put("Cover", conArticle.getCoverUrl());
        column.put("UserProfileAndNickname", user);
        column.put("ReadNumber", conArticle.getReadNumber());
        column.put("TitlesAndTopicId", titlesAndTopicIds);
        return R.ok(column);
    }

    @Override
    public R<?> articleMigration() {
        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(ConArticle::getCreator, migrationTestUserId);
        conArticleMapper.delete(lambdaUpdateWrapper);
        List<OldArticle> oldArticles = conArticleMapper.selectArticleFromOldDataSrc();
        List<ConArticle> result = new LinkedList<>();
        for (OldArticle oldArticle : oldArticles) {
            ConArticle conArticle = new ConArticle();
            conArticle.setTitle(oldArticle.getTitle());
            conArticle.setSubTitle(oldArticle.getTitle());
            conArticle.setCreateDate(oldArticle.getCreateDate());
            conArticle.setUpdateTime(oldArticle.getUpdateDate());
            String contentUrl = oldArticle.getContent();
            String coverImgUrl = oldArticle.getCoverUrl();
            String content = "";
//            if(oldArticle.getBolbData()!=null) {
//                content = new String(oldArticle.getBolbData());
//            }
            if (contentUrl.contains("/unicorn") && !contentUrl.contains("/unicorn_aged")) {
                contentUrl = "/unicorn_aged" + contentUrl.substring(8);
            }

            content = ArticleContentCrawler.contentCrawler(getCurrentServerIp() + contentUrl);
            content = ArticleContentCrawler.cleanImgLabelContent(getCurrentServerIp(), content);

            conArticle.setContent(content);

            if (coverImgUrl.contains("/filedownload") && !coverImgUrl.contains("http")) {
                coverImgUrl = getCurrentServerIp() + coverImgUrl;
            }
            conArticle.setCoverUrl(coverImgUrl);

            conArticle.setCreator(migrationTestUserId);
            conArticle.setUpdater(migrationTestUserId);
            String channelId = oldArticle.getChannelId();
            Long topicId = 1379994887029547010L;
            switch (channelId) {
                case "1017": {
                    topicId = 1383952101868072962L;
                    break;
                }
                case "1013": {
                    topicId = 1383952138496929793L;
                    break;
                }
                case "1014": {
                    topicId = 1383952163180408834L;
                    break;
                }
                case "1016": {
                    topicId = 1383952526369386497L;
                    break;
                }
            }

//            conArticle.setTopicId(topicId);
            conArticle.setLikeNumber(0);
            conArticle.setReadNumber(0);
            conArticle.setCommentNumber(0);
            conArticle.setDeleteFlag("0");
            result.add(conArticle);

            conArticleMapper.insert(conArticle);
        }

        return R.ok(result);
    }

    @Override
    public R<Map<String, Object>> save(ConArticle conArticle) {
        //插入操作
        conArticleMapper.insert(conArticle);
        //更新操作
        conArticleMapper.updateById(conArticle);

        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper
                .set(ConArticle::getDeleteFlag, "1")
                .eq(ConArticle::getId, 1);
        conArticleMapper.update(null, lambdaUpdateWrapper);

        //删除操作
        conArticleMapper.deleteById(1);

        //查询
        Page<ConArticle> page = new Page<>(1, 10);
        LambdaQueryWrapper<ConArticle> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ConArticle::getContent, ConArticle::getId).eq(ConArticle::getDeleteFlag, "0");
        Page<ConArticle> conArticlePage = conArticleMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = IPages.buildDataMap(conArticlePage);

        Page<ConArticle> page1 = new Page<>(1, 10);
        page1 = conArticleMapper.selectUserInfoAndArt(page, conArticle);
        return R.ok(map);
    }

}
