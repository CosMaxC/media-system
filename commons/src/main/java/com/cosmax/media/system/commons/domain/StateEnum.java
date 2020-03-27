package com.cosmax.media.system.commons.domain;

/**
 * @program: media-system
 * @description: sql状态枚举类
 * @author: Cosmax
 * @create: 2020/03/08 16:08
 */
public enum  StateEnum {
    CREATEORUPDATE(0,"createorupdate"), DELETE(1, "delete");

    private int id;

    private String name;

    StateEnum(int id, String name) {
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
