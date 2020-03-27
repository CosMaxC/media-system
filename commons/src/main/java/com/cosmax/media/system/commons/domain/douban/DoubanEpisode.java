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
public class DoubanEpisode implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 豆瓣id
     */
    @TableId
    private Long doubanId;

    /**
     * 豆瓣集数
     */
    private Integer doubanEpisode;

    /**
     * 本集中文名
     */
    private String nameCn;

    /**
     * 本集英文名
     */
    private String nameEn;

    /**
     * 播放时间
     */
    private String playTime;

    /**
     * 剧情简介
     */
    private String episodeBrief;


}
