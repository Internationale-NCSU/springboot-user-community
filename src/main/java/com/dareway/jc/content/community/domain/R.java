package com.dareway.jc.content.community.domain;


import com.dareway.jc.content.community.constant.Constants;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

import static com.dareway.jc.content.community.constant.Constants.RESULT_LIST;
import static com.dareway.jc.content.community.constant.Constants.TOTAL_COUNT;


/**
 * 响应信息主体
 *
 * @author lichp
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = Constants.FAIL;

    private int code;

    private String mess;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMess(msg);
        return apiResult;
    }


    public static Map<String, Object> buildDataMap(List list) {
        Map<String, Object> dataMap = new HashMap<>();
        if (list == null) {
            dataMap.put(TOTAL_COUNT, 0);
            dataMap.put(RESULT_LIST, new ArrayList<>());
        } else {
            dataMap.put(TOTAL_COUNT, list.size());
            dataMap.put(RESULT_LIST, list);
        }
        return dataMap;
    }

    public static Map<String, Object> buildDataMap(List list, Long total) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(TOTAL_COUNT, total);
        dataMap.put(RESULT_LIST, list);
        return dataMap;
    }

    public static Map<String, Object> buildDataMap(Set list) {
        Map<String, Object> dataMap = new HashMap<>();
        if (list == null) {
            dataMap.put(TOTAL_COUNT, 0);
            dataMap.put(RESULT_LIST, new ArrayList<>());
        } else {
            dataMap.put(TOTAL_COUNT, list.size());
            dataMap.put(RESULT_LIST, list);
        }
        return dataMap;
    }

    public static Map<String, Object> buildDataMap(Object object) {
        if (object == null) {
            return null;
        }
        List<Object> resultList = new ArrayList<>();
        resultList.add(object);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(TOTAL_COUNT, resultList.size());
        dataMap.put(RESULT_LIST, resultList);
        return dataMap;
    }
}
