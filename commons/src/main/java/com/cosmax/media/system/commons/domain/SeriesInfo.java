package com.cosmax.media.system.commons.domain;

import lombok.Data;

import java.util.List;

/**
 * @program: media-system
 * @description: 剧集实体类
 * @author: Cosmax
 * @create: 2020/02/03 18:53
 */

@Data
public class SeriesInfo {

    /**
     * 剧集名
     */
    private String name;

    /**
     * 季度
     */
    private Integer season;

    /**
     * 剧集目录
     */
    private String path;

    /**
     * 豆瓣id
     */
    private Long douban_id;

    /**
     * 集数信息
     */
    List<EpisodeInfo> episodeInfos;


}
