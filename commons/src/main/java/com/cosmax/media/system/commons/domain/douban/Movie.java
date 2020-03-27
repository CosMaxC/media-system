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
public class Movie implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 电影id
     */
    @TableId
    private Long movieId;

    /**
     * 电影文件名
     */
    private String movieName;

    /**
     * 电影位置
     */
    private String movieLocation;


}
