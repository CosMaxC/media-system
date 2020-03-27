package com.cosmax.media.system.file.provider.service.impl;

import com.cosmax.media.system.commons.domain.EpisodeInfo;
import com.cosmax.media.system.commons.domain.SeriesInfo;
import com.cosmax.media.system.commons.utils.MediaFileUtils;
import com.cosmax.media.system.commons.validation.FileValidation;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.file.api.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: media-system
 * @description: 实现file-api
 * @author: Cosmax
 * @create: 2020/02/02 17:13
 */

@Service
public class FileServiceImpl implements FileService {

    @Override
    public List<MovieInfo> getMovieInfosFromPath(String path) {

        File file = new File(path);
        if (file.exists()) {
            return searchMovie(file);
        }else {
            return new LinkedList<>();
        }
    }

    @Override
    public List<SeriesInfo> getSeriesInfosFormPath(String path) {
        File file = new File(path);

        if (file.exists()) {
            return searchSeries(file);
        }else {
            return new LinkedList<>();
        }
    }

    /**
     * 递归搜索剧集目录下视频文件
     * @param file {@link File}
     * @return List<SeriesInfo>
     */
    private List<SeriesInfo> searchSeries(File file) {

        LinkedList<SeriesInfo> seriesInfos = new LinkedList<>();
        // 判断file是否本就是电视剧集目录，目录下为剧集集数（Chernobyl.S01.1080p.BluRay）
        if (MediaFileUtils.getSeriesInfoByFile(file) != null){
            SeriesInfo seriesInfo = MediaFileUtils.getSeriesInfoByFile(file);
            if (seriesInfo != null){
                File[] files = file.listFiles();
                if (files == null){
                    return new LinkedList<>();
                }
                LinkedList<EpisodeInfo> list = new LinkedList<>();
                for (File file1 : files) {
                    if (file1.isFile() && FileValidation.checkVideo(file1.getAbsolutePath())) {
                        EpisodeInfo episodeInfoByFile = MediaFileUtils.getEpisodeInfoByFile(file1);
                        list.add(episodeInfoByFile);
                    }
                }
                seriesInfo.setEpisodeInfos(list);
                seriesInfos.add(seriesInfo);
            }

        }

        // 判断file目录下的文件
        LinkedList<File> list = new LinkedList<>();
        File[] files = file.listFiles();
        if (files == null){
            return new LinkedList<>();
        }
        for (File file2 : files) {
            if (file2.isDirectory()) {
                // path下获取文件夹
                list.add(file2);
            }
        }
        File temp_file;
        while (!list.isEmpty()) {
            temp_file = list.removeFirst();
            files = temp_file.listFiles();
            if (files == null){
                // 判断temp_file目录内是否有文件或文件夹，为空则忽视
                continue;
            }
            // 获取目录信息
            SeriesInfo seriesInfo = MediaFileUtils.getSeriesInfoByFile(temp_file);
            LinkedList<EpisodeInfo> episodeInfos = new LinkedList<>();
            if (seriesInfo == null){
                continue;
            }
            for (File file1 : files) {
                // 如果不为空，判断是否有目录，获取剧集信息
                if (file1.isDirectory()) {
                    list.add(file1);
                }
                if (file1.isFile() && FileValidation.checkVideo(file1.getAbsolutePath())) {
                    // 获取目录下集信息
                    EpisodeInfo episodeInfoByFile = MediaFileUtils.getEpisodeInfoByFile(file1);
                    episodeInfos.add(episodeInfoByFile);
                }
            }
            if (episodeInfos.size() > 0){
                seriesInfo.setEpisodeInfos(episodeInfos);
                seriesInfos.add(seriesInfo);
            }
        }
        return seriesInfos;
    }


    /**
     * 递归搜索电影目录下视频文件
     * @param file {@link File}
     * @return List<MovieInfo>
     */
    private List<MovieInfo> searchMovie(File file){
        File[] fs = file.listFiles();
        if (fs == null){
            return new LinkedList<>();
        }
        List<MovieInfo> movieList = new LinkedList<>();
        for(File f:fs){
            if(f.isDirectory())	{
                //如果是目录递归目录
                movieList.addAll(searchMovie(f));
            }
            if(f.isFile() && FileValidation.checkVideo(f.getAbsolutePath())) {
                // 如果是文件 判断是否为视频文件
                String absoluteFile = f.getAbsolutePath();
                // 截取路径获取电影信息实体
                MovieInfo movieInfo = MediaFileUtils.getMovieInfoByFile(absoluteFile);
                movieList.add(movieInfo);
            }
        }
        return movieList;
    }


}
