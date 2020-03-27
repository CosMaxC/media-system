package com.cosmax.media.system.commons.domain;

import lombok.Data;

/**
 * @program: media-system
 * @description: 电视电影枚举类
 * @author: Cosmax
 * @create: 2020/02/09 12:01
 */


public enum SubTypeEnum {

    MOVIE(0,"movie"), TV(1, "tv");

    private int id;

    private String name;

    SubTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
