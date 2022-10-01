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
public interface ConHomepageService {
    R<?> selectPagingHomePageContent(Integer current, Integer size, String type,String countyCode);

    R<?> selectPersonalHomePageContent(Integer current, Integer size);

    R<?> selectUserOriginalContent(Integer current, Integer size, String userId);

    R<?> selectHeaderAndPagingContent(Integer current, Integer size, String headerModuleType,String countyCode);
}
