package com.dareway.jc.content.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class User {
    private String nickname;
    private String profilePhotoUrl;
    private String userId;
}
