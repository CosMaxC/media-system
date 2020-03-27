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
public class DoubanWriter implements Serializable {

    private static final long serialVersionUID = 1151291818625903137L;

    /**
     * 豆瓣id
     */
    @TableId
    private Long doubanId;

    /**
     * 编剧id
     */
    private Long writerId;


}
