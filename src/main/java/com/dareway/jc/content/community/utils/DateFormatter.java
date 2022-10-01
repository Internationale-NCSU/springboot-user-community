package com.dareway.jc.content.community.utils;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateFormatter {

    String pattern() default "yyyy-MM-dd HH:mm:ss";

}
