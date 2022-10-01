package com.dareway.jc.content.community.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    private final String pattern;

    public DateSerializer(String pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String output = StringUtils.EMPTY;
        if (date != null) {
            output = new SimpleDateFormat(pattern).format(date);
        }
        jsonGenerator.writeString(output);
    }
}
