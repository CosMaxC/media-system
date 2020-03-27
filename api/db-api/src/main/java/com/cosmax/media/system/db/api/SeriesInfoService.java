package com.cosmax.media.system.db.api;

import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface SeriesInfoService extends IService<SeriesInfo> {

    /**
     * 根据媒体库id获取剧集信息
     * @param id 媒体库id
     * @return 剧集信息
     */
    List<SeriesInfo> getSeriesInfosByLibraryId(Long id);

    /**
     * 根据媒体库id删除剧集信息含集
     * @param mediaId 媒体库id
     */
    void deleteByMediaId(Long mediaId);
}
