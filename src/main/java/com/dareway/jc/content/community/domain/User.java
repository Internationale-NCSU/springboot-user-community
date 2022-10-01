package com.dareway.jc.content.community.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lichp
 * @since 2020-12-07
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userid;
    private String xm;
    private String nickname;
    private String photoUrl;
    private String deleteflag;
    private String yhqx;
    private String roleId;

    // 加入商户信息
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;

    private String storeName;
    private String storeIcon;
    private String storeChain;

    private List<String> permIds;


}
