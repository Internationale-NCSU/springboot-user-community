package com.dareway.jc.content.community.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */

@Data
public class CreateLikeParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long articleId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commentId;
    private String creator;
    private String updater;
}
