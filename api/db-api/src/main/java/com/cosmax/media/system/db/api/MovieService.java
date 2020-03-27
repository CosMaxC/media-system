package com.cosmax.media.system.db.api;

import com.cosmax.media.system.commons.domain.douban.Movie;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmax.media.system.commons.response.MovieResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface MovieService extends IService<Movie> {

    /**
     * 获取电影目录信息
     * @param id 媒体库id
     * @return 电影列表
     */
    List<Movie> getMovieInfosByLibraryId(Long id);

    /**
     * 根据媒体库id删除电影明细
     * @param mediaId 媒体库id
     */
    void deleteByMediaId(Long mediaId);

    /**
     * 根据媒体库id和电影名获取媒体库电影信息
     * @param mediaId 媒体库id
     * @param movieName 电影名
     * @return 电影实体
     */
    Movie getMovieInfosByLibraryIdAndMovieName(Long mediaId, String movieName);
}
