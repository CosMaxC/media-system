package com.cosmax.media.system.commons.utils;

import com.cosmax.media.system.commons.domain.EpisodeInfo;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.commons.domain.SeriesInfo;
import com.cosmax.media.system.commons.validation.FileValidation;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: media-system
 * @description: 媒体文件处理工具类
 * @author: Cosmax
 * @create: 2020/02/03 18:31
 */
public class MediaFileUtils {

    /**
     * 通过路径获取电影信息实体
     * @param absoluteFile 文件路径
     * @return MovieInfo {@link MovieInfo}
     */
    public static MovieInfo getMovieInfoByFile(String absoluteFile) {
        MovieInfo movieInfo = new MovieInfo();
        // 获取电影路径（含电影文件）
        movieInfo.setPath(absoluteFile);
        // 获取电影后缀
        movieInfo.setSuffix(FilenameUtils.getExtension(absoluteFile));
        // 获取电影名称信息
        String movieName = FilenameUtils.getBaseName(absoluteFile);
        Pattern pattern = Pattern.compile("^[\\w\\W]*(19[0-9]{2}|20[0-9]{2})");
        Matcher matcher = pattern.matcher(movieName);
        if (matcher.find()) {
            // 电影名
            movieName = matcher
                    .group(0)
                    .replaceAll("(19[0-9]{2}|20[0-9]{2})", "")
                    .replaceAll("[\\W]"," ");
            movieName = movieName.substring(0, movieName.length() - 1);
            movieInfo.setName(movieName);

            // 获取年份
            movieInfo.setYear(matcher.group(1));
        }
        return movieInfo;
    }

    /**
     * 通过路径获取电影信息实体
     * @param file 文件
     * @return MovieInfo {@link MovieInfo}
     */
    public static MovieInfo getMovieInfoByFile(File file) {
        MovieInfo movieInfo = new MovieInfo();
        String absolutePath = file.getAbsolutePath();
        // 获取电影路径（含电影文件）
        movieInfo.setPath(absolutePath);
        // 获取电影后缀
        movieInfo.setSuffix(FilenameUtils.getExtension(absolutePath));
        // 获取电影名称信息
        String movieName = FilenameUtils.getBaseName(absolutePath);
        Pattern pattern = Pattern.compile("^[\\w\\W]*(19[0-9]{2}|20[0-9]{2})");
        Matcher matcher = pattern.matcher(movieName);
        if (matcher.find()) {
            // 电影名
            movieName = matcher
                    .group(0)
                    .replaceAll("(19[0-9]{2}|20[0-9]{2})", "")
                    .replaceAll("[\\W]"," ");
            movieName = movieName.substring(0, movieName.length() - 1);
            movieInfo.setName(movieName);

            // 获取年份
            movieInfo.setYear(matcher.group(1));
        }
        return movieInfo;
    }

    /**
     * 通过file获取集信息
     * @param file 视频文件
     * @return EpisodeInfo
     */
    public static EpisodeInfo getEpisodeInfoByFile(File file){
        EpisodeInfo episodeInfo = new EpisodeInfo();
        String absolutePath = file.getAbsolutePath();
        // absolutePath
        if (!FileValidation.checkVideo(absolutePath)){
            return null;
        }
        // 获取电视剧名称信息
        String episodeName = FilenameUtils.getBaseName(absolutePath);
        Pattern pattern = Pattern.compile("(E|Ep|e|ep|EP|eP)\\d{2}");
        Matcher matcher = pattern.matcher(episodeName);
        if (matcher.find()) {
            String episode = matcher.group(0);
            episode = episode.replaceAll(matcher.group(1),"");
            episodeInfo.setPath(absolutePath);
            episodeInfo.setEpisode(Integer.valueOf(episode));
            episodeInfo.setSuffix(FilenameUtils.getExtension(absolutePath));
            return episodeInfo;
        }
        return null;
    }

    /**
     * 通过目录名获取剧集名
     * @param file 视频目录
     * @return String
     */
    public static SeriesInfo getSeriesInfoByFile(File file){

        String absolutePath = file.getAbsolutePath();

        // 获取电视剧名称信息
        String seriesName = FilenameUtils.getBaseName(absolutePath);
        Pattern pattern = Pattern.compile("^[\\w\\W]*[S|s]\\d{1,3}");
        Matcher matcher = pattern.matcher(seriesName);
        if (matcher.find()) {
            seriesName = matcher.group(0);

            Matcher matcher1 = Pattern.compile("[S|s]\\d{1,3}").matcher(seriesName);
            // 主要用来判断剧名.S01.xxxxx 是否符合标准
            if (matcher1.find()){
                SeriesInfo seriesInfo = new SeriesInfo();
                // 获取目录
                seriesInfo.setPath(absolutePath);

                // 获取季度
                String season = matcher1.group(0);
                seriesInfo.setSeason(Integer.parseInt(season.substring(1)));

                // 获取剧名
                String name = seriesName
                        .replaceAll(".[S|s]\\d{1,3}", "")
                        .replaceAll("\\W"," ");
                seriesInfo.setName(name);
                return seriesInfo;
            }
        }
        return null;
    }

    /**
     * 通过季度和集数拼接字符串为S01E02样式
     * @param season 季度
     * @param episode 集
     * @return 返回拼接字符串
     */
    public static String getSeasonEpisode(Integer season, Integer episode){

        DecimalFormat df = new DecimalFormat("00");
        String seasonStr = df.format(season);
        String episodeStr = df.format(episode);
        return "S" + seasonStr + "E" + episodeStr;
    }
}
