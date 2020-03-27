package com.cosmax.media.system.commons.domain;

import lombok.Data;

/**
 * @program: media-system
 * @description: 剧集的集信息
 * @author: Cosmax
 * @create: 2020/02/03 19:04
 */

@Data
public class EpisodeInfo {


    /**
     * 集数
     */
    private Integer episode;

    /**
     * 集数
     */
    private String suffix;

    /**
     * 路径
     */
    private String path;
//    /**
//     * 路径
//     */
//    private String linkPath;

}
