package com.dareway.jc.content.community.service;

import com.dareway.jc.content.community.domain.R;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public interface VideoTranscodingService {

    String videoTranscoding480(String videoUrl,Long articleId) throws IOException;

    String videoTranscoding720(String videoUrl,Long articleId) throws IOException;

    String videoTranscoding1080(String videoUrl,Long articleId) throws IOException;
}
