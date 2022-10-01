package com.dareway.jc.content.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dareway.jc.content.community.domain.ConFollow;

public interface ConFollowMapper extends BaseMapper<ConFollow> {

    Integer selectUserArticleTotalLikeNums(String userId);

    Integer selectUserCommentTotalLikeNums(String userId);

}
