package com.cosmax.media.system.db.api;

import com.cosmax.media.system.commons.domain.SeriesAndMovieQueryResponse;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
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
public interface MediaLibraryService extends IService<MediaLibrary> {

    /**
     * 获取可执行自动任务的媒体库列表
     * @param state 0|创建更新，1|删除
     * @return 可执行自动任务的媒体库列表
     */
    List<MediaLibrary> getScheduleList(Integer state);

    /**
     * 获取可执行豆瓣任务的最早一条媒体库信息
     * @return 可执行豆瓣任务的媒体库
     */
    MediaLibrary getLibraryByDoubanCondition();

    /**
     * 获取已经完成所有任务的媒体库list
     * @return List<MediaLibrary>
     */
    List<MediaLibrary> getFinishedLibrary();

    /**
     * 根据媒体库id获取剧集或电影信息实体list
     * @param id 媒体库id
     * @return List<SeriesAndMovieQueryResponse>
     */
    List<SeriesAndMovieQueryResponse> getSeriesAndMovieQueryResponseById(Long id);

    /**
     * 根据媒体库id删除于此相关的媒体库视频信息（movie表、movie_video_douban表、剧集表、集数表）
     * @param id 媒体库id
     */
    Boolean deleteMediaVideosAndDoubanInfosById(Long id);
}
