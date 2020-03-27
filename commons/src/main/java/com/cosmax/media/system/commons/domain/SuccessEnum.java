package com.cosmax.media.system.commons.domain;

/**
 * @program: media-system
 * @description: sql成功状态枚举类
 * @author: Cosmax
 * @create: 2020/03/08 16:10
 */
public enum SuccessEnum {

    UNSUCCESS(0, "unsuccess"),SUCCESS(1,"success"), EXCEPTION(2, "exception");
    private int id;

    private String name;

    SuccessEnum(int id, String name) {
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
