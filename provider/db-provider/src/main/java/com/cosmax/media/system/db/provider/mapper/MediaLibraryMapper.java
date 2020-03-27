package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.SeriesAndMovieQueryResponse;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
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
public interface MediaLibraryMapper extends BaseMapper<MediaLibrary> {

    @Select("SELECT ml.id, ml.`name`, ml.`directory`, ml.type FROM media_library AS ml INNER JOIN media_library_schedule AS mls ON ml.id = mls.library_id WHERE mls.state = #{state} AND mls.is_success = 0")
    List<MediaLibrary> getScheduleList(@Param("state") Integer state);

    /**
     * 获取可执行豆瓣任务的最早一条媒体库信息
     * @return 可执行豆瓣任务的媒体库
     */
    @Select(" SELECT " +
            "ml.id, " +
            "ml.`name`, " +
            "ml.`directory`, " +
            "ml.type " +
            "FROM " +
            "media_library AS ml " +
            "INNER JOIN media_library_schedule AS mls ON ml.id = mls.library_id " +
            "INNER JOIN media_douban_schedule AS mds ON ml.id = mds.media_id " +
            "WHERE " +
            "mls.state = 0 AND " +
            "mls.is_success = 1 AND " +
            "mds.state = 0 " +
            "ORDER BY " +
            "ml.id ASC " +
            "LIMIT 1  ")
    MediaLibrary getLibraryByDoubanCondition();

    /**
     * 获取已经完成所有任务的媒体库list
     * @return List<MediaLibrary>
     */
    @Select("SELECT " +
            "ml.id, " +
            "ml.`name`, " +
            "ml.`directory`, " +
            "ml.type " +
            "FROM " +
            "media_library AS ml " +
            "INNER JOIN media_library_schedule AS mls ON ml.id = mls.library_id " +
            "INNER JOIN media_douban_schedule AS mds ON ml.id = mds.media_id " +
            "WHERE " +
            "mds.state = 1 AND " +
            "mls.is_success = 1 ")
    List<MediaLibrary> getFinishedLibrary();

    /**
     * 根据媒体库id获取剧集或电影信息实体list
     * @param id 媒体库id
     * @return List<SeriesAndMovieQueryResponse>
     */
    @Select("select * from ( " +
            "SELECT " +
            "m.movie_id id, " +
            "m.movie_name `name`, " +
            "m.movie_location location, " +
            "di.avatar_path imgPath " +
            "FROM " +
            "movie AS m " +
            "INNER JOIN media_video_douban AS mvd ON m.movie_id = mvd.item_id " +
            "LEFT JOIN douban_info AS di ON di.douban_id = mvd.douban_id " +
            "WHERE " +
            "mvd.media_id = #{id} " +
            "union all " +
            "SELECT " +
            "si.series_id AS id, " +
            "si.series_name AS `name`, " +
            "si.series_location AS location, " +
            "di.avatar_path AS imgPath " +
            "FROM " +
            "series_info AS si " +
            "INNER JOIN media_video_douban AS mvd ON si.series_id = mvd.item_id " +
            "left JOIN douban_info AS di ON di.douban_id = mvd.douban_id " +
            "WHERE " +
            "mvd.media_id = #{id} " +
            ") a " +
            "order by a.name ")
    List<SeriesAndMovieQueryResponse> getSeriesAndMovieQueryResponseById(@Param("id") Long id);

    /**
     * 根据媒体库id删除于此相关的媒体库视频信息（movie表、movie_video_douban表、剧集表、集数表）
     * @param id 媒体库id
     */
    @Delete("DELETE m, mvd, si, se " +
            "FROM " +
            "media_library AS ml " +
            "LEFT JOIN media_video_douban AS mvd ON ml.id = mvd.media_id " +
            "LEFT JOIN movie m on m.movie_id = mvd.item_id " +
            "LEFT JOIN series_info AS si ON mvd.item_id = si.series_id " +
            "LEFT JOIN series_episode AS se ON si.series_id = si.series_id " +
            "WHERE " +
            "ml.id = #{id} ")
    void deleteMediaVideosAndDoubanInfosById(@Param("id") Long id);
}
