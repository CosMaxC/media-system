package com.cosmax.media.system.commons.domain;

import com.cosmax.media.system.commons.domain.douban.Movie;
import lombok.Data;

/**
 * @program: media-system
 * @description: 根据媒体库id返回的电影实体
 * @author: Cosmax
 * @create: 2020/03/16 15:34
 */

@Data
public class MovieQueryResponse extends Movie {
    private static final long serialVersionUID = 8702019432066262504L;

    private String img;
}
