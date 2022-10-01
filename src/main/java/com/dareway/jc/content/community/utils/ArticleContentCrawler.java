package com.dareway.jc.content.community.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * <p>
 *  旧文章Jsp页面内容爬取与清洗
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 *
 * */
public class ArticleContentCrawler {
    public static String contentCrawler(String url){
        String content="";
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("content");
            content = elements.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }
    public static String cleanImgLabelContent(String prefix,String html){
        Document document = Jsoup.parse(html);
        for(Element e:document.getElementsByTag("img")){
            if(e.attr("src").contains("http")){
                continue;
            }
            String newValue = prefix + e.attr("src");
            e.attr("src",newValue);
        }
        return document.getElementsByClass("content").toString();
    }
}
