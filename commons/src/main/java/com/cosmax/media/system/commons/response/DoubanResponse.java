package com.cosmax.media.system.commons.response;

import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import com.cosmax.media.system.commons.domain.douban.Star;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: media-system
 * @description: 豆瓣返回实体
 * @author: Cosmax
 * @create: 2020/03/04 13:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DoubanResponse extends DoubanInfo {

    /**
     * 导演list
     */
    List<Star> directorList;

    /**
     * 编剧list
     */
    List<Star> writerList;

    /**
     * 演员list
     */
    List<Star> starList;
}
