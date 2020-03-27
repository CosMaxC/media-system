package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.douban.Movie;
import com.cosmax.media.system.commons.response.MovieResponse;
import com.cosmax.media.system.db.provider.mapper.MovieMapper;
import com.cosmax.media.system.db.api.MovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Override
    public List<Movie> getMovieInfosByLibraryId(Long id) {
        return movieMapper.getMovieInfosByLibraryId(id);
    }

    @Override
    public void deleteByMediaId(Long mediaId) {
        movieMapper.deleteByMediaId(mediaId);
    }

    @Override
    public Movie getMovieInfosByLibraryIdAndMovieName(Long mediaId, String movieName) {
        return movieMapper.getMovieInfosByLibraryIdAndMovieName(mediaId, movieName);
    }

}
