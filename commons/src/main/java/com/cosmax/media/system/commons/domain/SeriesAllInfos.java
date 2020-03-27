package com.cosmax.media.system.commons.domain;

import com.cosmax.media.system.commons.response.SeasonEpisode;
import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: media-system
 * @description: 剧集信息以及剧集位置
 * @author: Cosmax
 * @create: 2020/03/04 11:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeriesAllInfos extends SeriesInfo {
    private static final long serialVersionUID = -5859525483328095581L;

    /**
     * 季度剧集信息
     * 效果类似：
     * {
     *     season: 1,
     *     seriesEpisodes: [
     *     {
     *         id: 1001,
     *         season: 1,
     *         episode: 1,
     *         path: "D://1.mp4"
     *     },
     *     {
     *          id: 1001,
     *          season: 1,
     *          episode: 2,
     *          path: "D://2.mp4"
     *      }
     *     ]
     * }
     */
    List<SeasonEpisode> seasonEpisodes;

}
