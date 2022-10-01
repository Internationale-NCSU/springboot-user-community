package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dareway.jc.content.community.domain.ConFunction;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConFunctionMapper;
import com.dareway.jc.content.community.service.ConFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ConFunctionServiceImpl implements ConFunctionService {
    @Autowired
    private ConFunctionMapper conFunctionMapper;

    @Override
    public R<?> addFunction(String funcName, String tagUrl) {
        ConFunction conFunction = new ConFunction();
        conFunction.setFuncName(funcName);
        conFunction.setTagUrl(tagUrl);
        conFunctionMapper.insert(conFunction);
        return R.ok();
    }

    @Override
    public R<?> deleteFunction(Long id) {
        ConFunction conFunction = conFunctionMapper.selectById(id);
        if (conFunction == null) {
            return R.fail("未找到标签");
        }
        conFunctionMapper.deleteById(id);
        return R.ok();
    }

    @Override
    public R<?> editFunction(Long id, String funcName, String tagUrl, String pageName, String params) {
        LambdaUpdateWrapper<ConFunction> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper
                .eq(ConFunction::getId, id)
                .set(ConFunction::getFuncName, funcName)
                .set(ConFunction::getTagUrl, tagUrl)
                .set(ConFunction::getParams,params)
                .set(ConFunction::getPageName,pageName);
        conFunctionMapper.update(null, lambdaUpdateWrapper);
        return R.ok("");
    }

    @Override
    public R<?> selectFunctionList() {
        LambdaQueryWrapper<ConFunction> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select();
        List<ConFunction> result = conFunctionMapper.selectList(lambdaQueryWrapper);
        return R.ok(result);
    }

    @Override
    public R<?> selectGroupFunctionList(List<Long> ids) {
        List<ConFunction> result = new LinkedList<>();
        for (Long id : ids) {
            ConFunction conFunction = conFunctionMapper.selectById(id);
            if (conFunction != null) {
                result.add(conFunction);
            }
        }
        return R.ok(result);
    }
}
