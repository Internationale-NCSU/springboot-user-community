package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.pojo.Article.SelectArticlesConditions;
import com.dareway.jc.content.community.pojo.Management.SearchArticlePage;
import com.dareway.jc.content.community.service.ConManagementService;
import com.dareway.jc.content.community.utils.DateUtils;
import com.dareway.jc.content.community.utils.IPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConManagementServiceImpl implements ConManagementService {
    @Autowired
    ConArticleMapper conArticleMapper;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
    @Override
    public R<?> selectArticlesByCondition(SelectArticlesConditions sac) {

        Page<SearchArticlePage> page = new Page<>(sac.getCurrent(),sac.getSize());
        Page<SearchArticlePage> articlePage = conArticleMapper.selectArticlesByCondition(page,sac);
        Map<String,Object> result = IPages.buildDataMap(articlePage);
        return R.ok(result);
    }
}
