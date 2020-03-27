package com.cosmax.media.system.db.api;

import com.cosmax.media.system.commons.domain.douban.SeriesEpisode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmax.media.system.commons.response.SeasonEpisode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface SeriesEpisodeService extends IService<SeriesEpisode> {

    /**
     * 根据媒体库id获取每集季度路径信息
     * @param id 媒体库id
     * @return 剧集list
     */
    List<SeriesEpisode> getEpisodesInfosByLibraryId(Long id);
    /**
     * 根据剧集id获取季度每集路径信息
     * @param id 剧集id
     * @return 剧集list
     */
    List<SeasonEpisode> getEpisodesInfosBySeriesId(Long id);

    /**
     * 根据媒体库id，剧集名和集删除集信息
     * @param mediaId 媒体库id
     * @param seriesName 剧集名 XXX 第1季
     * @param episode 集数
     * @return 影响行数
     */
    int deleteSeriesEpisodeByMediaIdAndSeriesIdAndEpisode(Long mediaId, String seriesName, Integer episode);

    /**
     * 根据媒体库id,剧集名和集数获取集信息地址
     *
     * @param id 媒体库id
     * @param seriesName 剧集名
     * @param episode 集数
     * @return 剧集信息 {@link SeriesEpisode}
     */
    SeriesEpisode getEpisodesInfosBySeriesNameAndEpisode(Long id, String seriesName, Integer episode);


}
