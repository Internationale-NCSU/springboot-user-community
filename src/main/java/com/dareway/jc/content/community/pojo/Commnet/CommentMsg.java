package com.dareway.jc.content.community.pojo.Commnet;

import com.dareway.jc.content.community.pojo.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentMsg {
    private User creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commentDate;

    private String content;

    private Integer likeNumber;

    private List<ReplyMsg> children;

    private String formatTime;

    private boolean isLikeToComment;
}
