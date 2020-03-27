package com.cosmax.media.system.file.api;

import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.commons.domain.SeriesInfo;

import java.io.File;
import java.util.List;

/**
 * @program: media-system
 * @description: 文件操作api
 * @author: Cosmax
 * @create: 2020/02/02 16:57
 */
public interface FileService {

    /**
     * 获取电影信息实体list
     * 同一规格： 影片名.年份.xxxxxxx.后缀
     * @param path 视频路径
     * @return List<MovieInfo>
     */
    List<MovieInfo> getMovieInfosFromPath(String path);


    /**
     * 获取剧集信息实体list
     * 同一规格： 影片名.S(季度).xxxxxxx
     * * episode 规格：影片名.S(季度)E(集数).xxxxxxx
     * @param path 视频路径
     * @return List<MovieInfo>
     */
    List<SeriesInfo> getSeriesInfosFormPath(String path);



}
