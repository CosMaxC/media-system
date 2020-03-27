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
 * @since 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MediaDoubanSchedule implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 媒体库id
     */
    @TableId
      private Long mediaId;

    /**
     * 状态：0|未搜索，1|已搜索
     */
    private Integer state;


}
