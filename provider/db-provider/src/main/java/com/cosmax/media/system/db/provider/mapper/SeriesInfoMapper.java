package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface SeriesInfoMapper extends BaseMapper<SeriesInfo> {

    /**
     * 根据媒体库id获取剧集信息
     * @param id 媒体库id
     * @return 剧集列表
     */

    @Select("SELECT si.series_id seriesId,   " +
            "si.series_name seriesName,   " +
            "si.series_location seriesLocation " +
            "FROM series_info AS si   " +
            "INNER JOIN media_video_douban AS mvd ON si.series_id = mvd.item_id   " +
            "WHERE mvd.media_id = #{id}  " +
            "order by si.series_id ASC")
    List<SeriesInfo> getSeriesInfosByLibraryId(Long id);

    /**
     * 根据媒体库id删除剧集信息含集
     * @param mediaId 媒体库id
     */
    @Delete("DELETE media_video_douban, series_info, series_episode " +
            "FROM " +
            "media_video_douban " +
            "INNER JOIN series_info ON media_video_douban.item_id = series_info.series_id " +
            "INNER JOIN series_episode ON series_info.series_id = series_episode.series_id " +
            "WHERE " +
            "media_video_douban.media_id = #{id}")
    void deleteByMediaId(@Param("id") Long mediaId);
}
