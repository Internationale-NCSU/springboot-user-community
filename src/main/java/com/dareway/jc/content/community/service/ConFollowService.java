package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.R;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
public interface ConFollowService {
    R<?> followOrUnfollowTheAuthor(String id);

    R<?> selectUserFollowList();

    R<?> selectUserFollowerList();

    int selectUserFollowerNums(String userId);

    int selectUserFollowNums(String userId);

    int selectUserTotalLikeNums(String userId);

    boolean isFollowTheAuthor(String authorUserId);
}
