package com.dareway.jc.content.community.config;


import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @description druid 配置
 * @author lichp
 * @create 2019-05-13 9:39
 */
@Configuration
public class DruidConfig {

    //配置druid的监控
    //1配置一个管理后台的servlet
    @Bean
    public ServletRegistrationBean startViewSevlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> initParams = new HashMap<>();

        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","Dareway@2020Druid");
        initParams.put("allow","");//默认就是访问所有
        initParams.put("resetEnable","false");//// 禁用HTML页面上的“Reset All”功能
        //@WebInitParam(name = "deny", value = "192.168.1.73"),// IP黑名单 (存在共同时，deny优先于allow)

        bean.setInitParameters(initParams);
        return bean;
    }


    //2配置一个监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");


        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return  bean;
    }
}
