package com.cosmax.media.system.commons.domain;

import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import lombok.Data;

/**
 * @program: media-system
 * @description: 根据媒体库id返回的剧集实体
 * @author: Cosmax
 * @create: 2020/03/16 15:32
 */

@Data
public class SeriesAndMovieQueryResponse {
    private static final long serialVersionUID = -238371056202306666L;
    /**
     * 电影或剧集id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String location;
    /**
     * 图片路径
     */
    private String imgPath;
}
