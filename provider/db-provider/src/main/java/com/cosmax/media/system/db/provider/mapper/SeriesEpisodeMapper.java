package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.douban.SeriesEpisode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cosmax.media.system.commons.response.SeasonEpisode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface SeriesEpisodeMapper extends BaseMapper<SeriesEpisode> {

    /**
     * 根据媒体库id获取每集季度路径信息
     * @param id 媒体库id
     * @return 剧集list
     */
    @Select("SELECT  " +
            "se.series_id,  " +
            "se.season,  " +
            "se.episode,  " +
            "se.episode_location  " +
            "FROM  " +
            "series_episode AS se  " +
            "INNER JOIN media_video_douban AS mvd ON se.series_id = mvd.item_id  " +
            "WHERE " +
            "mvd.media_id = #{id}  " +
            "ORDER BY  " +
            "se.series_id ASC, " +
            "se.season ASC,  " +
            "se.episode ASC")
    List<SeriesEpisode> getEpisodesInfosByLibraryId(@Param("id") Long id);

    /**
     * 根据剧集id获取季度每集路径信息
     * @param id 剧集id
     * @return 剧集list
     */
    List<SeasonEpisode> getEpisodesInfosBySeriesId(@Param("id") Long id);

    /**
     * 根据剧集id和季度获取每集路径信息
     * @param id 剧集id
     * @return 剧集list
     */
    List<SeasonEpisode> getEpisodesInfosBySeriesIdAndSeason(@Param("id") Long id, @Param("season") Integer season);

    /**
     * 根据媒体库id，剧集名和集删除集信息
     * @param mediaId 媒体库id
     * @param seriesName 剧集名 XXX 第1季
     * @param episode 集数
     * @return 影响行数
     */
    @Delete("delete se " +
            "FROM " +
            "series_episode AS se " +
            "INNER JOIN series_info AS si ON se.series_id = si.series_id " +
            "INNER JOIN media_video_douban AS mvd ON si.series_id = mvd.item_id " +
            "WHERE " +
            "mvd.media_id = #{mediaId} AND " +
            "si.series_name = #{seriesName} AND " +
            "se.episode = #{episode}")
    int deleteSeriesEpisodeByMediaIdAndSeriesIdAndEpisode(@Param("mediaId") Long mediaId, @Param("seriesName") String seriesName, @Param("episode") Integer episode);

    /**
     * 根据媒体库id,剧集名和集数获取集信息地址
     *
     * @param id 媒体库id
     * @param seriesName 剧集名
     * @param episode 集数
     * @return 剧集信息 {@link SeriesEpisode}
     */
    @Select("SELECT " +
            "se.series_id, " +
            "se.season, " +
            "se.episode, " +
            "se.episode_location " +
            "FROM " +
            "series_episode AS se " +
            "INNER JOIN series_info AS si ON se.series_id = si.series_id " +
            "INNER JOIN media_video_douban AS mvd ON si.series_id = mvd.item_id " +
            "WHERE " +
            "si.series_name = #{seriesName} AND " +
            "mvd.media_id = #{id} AND " +
            "se.episode = #{episode} ")
    SeriesEpisode getEpisodesInfosBySeriesNameAndEpisode(@Param("id")Long id, @Param("seriesName")String seriesName, @Param("episode")Integer episode);
}
