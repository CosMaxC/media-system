package com.cosmax.media.system.commons.domain.douban;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeriesEpisode implements Serializable {

    private static final long serialVersionUID = -6975340496661560795L;

    /**
     * 电视剧id
     */
    @TableId
    private Long seriesId;

    /**
     * 电视剧季度
     */
    private Integer season;

    /**
     * 电视剧集数
     */
    private Integer episode;

    /**
     * 集位置
     */
    private String episodeLocation;


}
