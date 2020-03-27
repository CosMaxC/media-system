package com.cosmax.media.system.commons.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * douban_api
 * @author
 */
@Data
public class DoubanApi implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * apikey
     */
    private String apikey;

    /**
     * 是否可以搜索，0为否，1为是
     */
    private Integer isSearch;

    private static final long serialVersionUID = 1L;
}
