package com.cosmax.media.system.commons.response;

import com.cosmax.media.system.commons.domain.douban.SeriesEpisode;
import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @program: media-system
 * @description: 数据库返回的剧集类豆瓣路径等信息实体
 * @author: Cosmax
 * @create: 2020/03/04 13:15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeriesResponse implements Serializable {

    private static final long serialVersionUID = -9149207267070863772L;
    /**
     * 剧集信息
     */
    private SeriesInfo seriesInfo;
    /**
     * 集信息
     */
    private List<SeriesEpisode> seriesEpisodes;
    /**
     * 豆瓣信息
     */
    private DoubanResponse doubanResponse;

}
