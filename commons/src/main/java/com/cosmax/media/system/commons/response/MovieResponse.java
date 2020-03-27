package com.cosmax.media.system.commons.response;

import com.cosmax.media.system.commons.domain.douban.Movie;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: media-system
 * @description: 电影信息实体
 * @author: Cosmax
 * @create: 2020/03/04 13:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MovieResponse implements Serializable {
    private static final long serialVersionUID = 4372471235438584910L;

    private Movie movieInfo;
    private DoubanResponse doubanResponse;
}
