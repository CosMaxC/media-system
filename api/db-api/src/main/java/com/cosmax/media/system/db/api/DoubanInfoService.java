package com.cosmax.media.system.db.api;

import com.cosmax.media.system.commons.domain.DoubanAllInfo;
import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmax.media.system.commons.response.DoubanResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface DoubanInfoService extends IService<DoubanInfo> {

    /**
     * 根据媒体库id豆瓣信息
     * @param id 媒体库id
     * @return 豆瓣信息实体
     */
    DoubanAllInfo getDoubanInfosByLibraryId(Long id);

    /**
     * 根据视频id豆瓣信息
     * @param id 视频id
     * @return 豆瓣返回信息实体
     */
    DoubanResponse getDoubanResponseByVideosId(Long id);
}
