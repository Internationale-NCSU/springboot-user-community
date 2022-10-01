package com.dareway.jc.content.community.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

import static com.dareway.jc.content.community.constant.Constants.RESULT_LIST;
import static com.dareway.jc.content.community.constant.Constants.TOTAL_COUNT;


/**
 * 组装我们需要的分页信息
 * @author lichp
 * @version 1.0.0  2020/10/22 10:18
 * @since JDK1.8
 */
public class IPages {

    public static Map<String, Object> buildDataMap(IPage<?> iPage) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(TOTAL_COUNT, iPage.getTotal());
        dataMap.put(RESULT_LIST, iPage.getRecords());
        return dataMap;
    }

}
