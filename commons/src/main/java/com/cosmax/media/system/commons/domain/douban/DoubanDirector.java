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
public class DoubanDirector implements Serializable {


    private static final long serialVersionUID = 3783297212661858584L;
    /**
     * 豆瓣id
     */
    @TableId
    private Long doubanId;

    /**
     * 导演id
     */
    private Long directorId;


}
