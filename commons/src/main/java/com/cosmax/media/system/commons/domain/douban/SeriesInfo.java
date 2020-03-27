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
public class SeriesInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 电视剧id
     */
    @TableId
    private Long seriesId;

    /**
     * 电视剧文件名
     */
    private String seriesName;

    /**
     * 电视剧位置
     */
    private String seriesLocation;


}
