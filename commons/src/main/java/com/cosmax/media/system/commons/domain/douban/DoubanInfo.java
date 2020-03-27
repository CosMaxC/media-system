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
public class DoubanInfo implements Serializable {

    private static final long serialVersionUID = -5627375820879487654L;

    /**
     * 豆瓣id
     */
    @TableId
    private Long doubanId;

    /**
     * 星星数，40为4颗星，45为4.5颗星
     */
    private Integer starCount;

    /**
     * 豆瓣评分
     */
    private Float score;

    /**
     * 制片地区
     */
    private String madeBy;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 片长
     */
    private String costTime;

    /**
     * 上映日期
     */
    private String onTime;

    /**
     * 译名
     */
    private String engName;

    /**
     * 豆瓣链接
     */
    private String doubanLink;

    /**
     * 豆瓣头像本地地址
     */
    private String avatarPath;

    /**
     * 豆瓣海报地址第一个
     */
    private String posterPath;

    /**
     * 条目类型：0为电影，1为电视剧
     */
    private Integer subType;

    /**
     * 中文名
     */
    private String cnName;

    /**
     * 介绍
     */
    private String brief;


}
