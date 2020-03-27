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
public class MediaVideoDouban implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 媒体库id
     */
    @TableId
    private Long mediaId;

    /**
     * 电影或电视剧id
     */
    private Long itemId;

    /**
     * 豆瓣id
     */
    private Long doubanId;


}
