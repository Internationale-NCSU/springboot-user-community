package com.dareway.jc.content.community.pojo.Commnet;

import com.dareway.jc.content.community.pojo.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 封装入参/出参
 * </p>
 *
 * @author WangPx
 * @since 2021-04-09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyMsg implements Comparable<ReplyMsg> {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private User creator;

    private User recipient;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyDate;

    private String content;

    private Integer likeNumber;

    private Boolean isLikeToComment;

    private String formatTime;

    @Override
    public int compareTo(ReplyMsg o1) {
        return o1.getReplyDate().compareTo(this.getReplyDate());
    }
}
