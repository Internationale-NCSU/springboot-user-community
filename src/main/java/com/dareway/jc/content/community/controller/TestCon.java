package com.dareway.jc.content.community.controller;

import com.dareway.jc.content.community.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lichp
 * @version 1.0.0  2021/5/6 16:08
 * @since JDK1.8
 */
@RestController
public class TestCon {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/test")
    public void user() {
        System.out.println(securityService.getLoginUser());
    }
}
