package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.R;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
public interface ConFunctionService {
    R<?> addFunction(String funcName, String tagUrl);

    R<?> selectFunctionList();

    R<?> selectGroupFunctionList(List<Long> id);

    R<?> deleteFunction(Long id);

    R<?> editFunction(Long id, String funcName, String tagUrl, String pageName, String params);
}
