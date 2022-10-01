package com.dareway.jc.content.community.utils;

public class ProfileJudger {
    public static boolean isNullProfile(String s){
        if(s.equals("https://unicornaged.darewayhealth.com/filedownload/unicorn_aged/photo/")){
            return true;
        }else {
            return false;
        }
    }
}
