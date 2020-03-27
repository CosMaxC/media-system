package com.cosmax.media.system.commons.domain;

import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: media-system
 * @description: 豆瓣所有信息，用于查询所有内容
 * @author: Cosmax
 * @create: 2020/03/03 23:09
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DoubanAllInfo extends DoubanInfo {

    private String writerInfo;
    private String directorInfo;
    private String starInfo;


}
