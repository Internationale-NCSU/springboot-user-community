package com.dareway.jc.content.community.utils;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class DateFormatterAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 7368707128625539909L;

    @Override
    public Object findSerializer(Annotated annotated) {
        DateFormatter formatter = annotated.getAnnotation(DateFormatter.class);
        if (formatter != null) {
            return new DateSerializer(formatter.pattern());
        }
        return super.findSerializer(annotated);
    }
}
