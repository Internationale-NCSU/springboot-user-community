package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.Article.SelectArticlesConditions;

public interface ConManagementService {
    R<?> selectArticlesByCondition(SelectArticlesConditions sac);
}
