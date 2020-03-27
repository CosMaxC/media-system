package com.cosmax.media.system.commons.domain;

import lombok.Data;

/**
 * @program: media-system
 * @description: 电影文件信息实体
 * @author: Cosmax
 * @create: 2020/02/03 12:07
 */

@Data
public class MovieInfo {

    /**
     * 电影名
     */
    private String name;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 电影位置
     */
    private String path;

//    /**
//     * 硬链接位置
//     */
//    private String linkPath;

    /**
     * 电影年份
     */
    private String year;

    /**
     * 豆瓣id
     */
    private Long douban_id;
}
