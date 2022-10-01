package com.dareway.jc.content.community.pojo.Article;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
@Data
public class OldArticle {
    private String title;
    private String subtitle;
    private Date createDate;
    private Date updateDate;
    private String content;
    private String coverUrl;
    private String channelId;
    private byte[] bolbData;
}
