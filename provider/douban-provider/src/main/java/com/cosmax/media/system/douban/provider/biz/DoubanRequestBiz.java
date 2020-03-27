package com.cosmax.media.system.douban.provider.biz;

import com.cosmax.media.system.commons.domain.DoubanVideoInfo;
import com.cosmax.media.system.commons.domain.douban.Star;

/**
 * @program: media-system
 * @description: 豆瓣各种请求接口
 * @author: Cosmax
 * @create: 2020/02/07 12:14
 */
public interface DoubanRequestBiz {

    /**
     * 通过演员id获取演员资料
     * @param id 演员id
     * @param apikey 密钥
     * @return 演员信息实体 {@link Star}
     */
    Star getStarByStarId(Long id, String apikey);

    /**
     * 根据豆瓣id获取豆瓣信息
     * @param id 豆瓣id
     * @param apikey 密钥
     * @return 豆瓣信息实体 {@link DoubanVideoInfo}
     */
    DoubanVideoInfo getDoubanInfoByDoubanId(Long id, String apikey);
}
