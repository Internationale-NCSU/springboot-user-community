package com.dareway.jc.content.community.pojo.Topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class TitlesAndTopicId {
    String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long topicId;
}
