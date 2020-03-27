package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.douban.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cosmax.media.system.commons.response.MovieResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Component
public interface MovieMapper extends BaseMapper<Movie> {

    /**
     * 获取媒体库电影信息
     * @param id 媒体库id
     * @return 电影列表
     */

    @Select("SELECT m.movie_id movieId, m.movie_name movieName FROM movie AS m INNER JOIN media_video_douban AS mvd ON m.movie_id = mvd.item_id WHERE mvd.media_id = #{id} ")
    List<Movie> getMovieInfosByLibraryId(@Param("id") Long id);

    /**
     * 根据媒体库id删除电影明细
     * @param mediaId 媒体库id
     */
    @Delete("DELETE movie, media_video_douban " +
            "FROM " +
            "movie " +
            "INNER JOIN media_video_douban ON media_video_douban.item_id = movie.movie_id " +
            "WHERE " +
            "media_video_douban.media_id = #{id} ")
    void deleteByMediaId(@Param("id") Long mediaId);

    /**
     * 根据媒体库id和电影名获取媒体库电影信息
     * @param mediaId 媒体库id
     * @param movieName 电影名
     * @return 电影实体
     */
    @Select("SELECT " +
            "m.movie_id, " +
            "m.movie_name, " +
            "m.movie_location " +
            "FROM " +
            "movie AS m " +
            "INNER JOIN media_video_douban AS mvd ON m.movie_id = mvd.item_id " +
            "WHERE " +
            "mvd.media_id = #{mediaId} AND " +
            "m.movie_name = #{movieName} ")
    Movie getMovieInfosByLibraryIdAndMovieName(@Param("mediaId") Long mediaId, @Param("movieName") String movieName);
}
