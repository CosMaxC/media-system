package com.cosmax.media.system.commons.domain;

import com.cosmax.media.system.commons.domain.douban.*;
import lombok.Data;

import java.util.List;

/**
 * @program: media-system
 * @description: 豆瓣视频信息实体
 * @author: Cosmax
 * @create: 2020/02/05 22:00
 */

@Data
public class DoubanVideoInfo {

    // 剧集本身信息
    private DoubanInfo doubanInfo;

    // 豆瓣名，是连接豆瓣信息和视频信息的标识
    private String videoName;

    // 导演id
    private List<Long> directorList;

    // 演员id
    private List<Long> actorList;

    // 编剧id
    private List<Long> writerList;


}
