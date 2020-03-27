package com.cosmax.media.system.commons.response;

import com.cosmax.media.system.commons.domain.douban.SeriesEpisode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @program: media-system
 * @description: 季度剧集实体
 * @author: Cosmax
 * @create: 2020/03/04 12:00
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeasonEpisode implements Serializable {
    private static final long serialVersionUID = -1329386715730102610L;
    /**
     * 电视剧id
     */
    private Long seriesId;
    /**
     * 季度id
     */
    private Integer seasonNo;
    /**
     * 剧集list
     */
    List<SeriesEpisode> seriesEpisodes;
}
