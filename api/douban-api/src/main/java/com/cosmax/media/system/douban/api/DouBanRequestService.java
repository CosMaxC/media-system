package com.cosmax.media.system.douban.api;

import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.commons.domain.SeriesInfo;

import java.util.List;

/**
 * @program: media-system
 * @description: 豆瓣相关api
 * @author: Cosmax
 * @create: 2020/02/05 11:15
 */
public interface DouBanRequestService {

    /**
     * 通过遍历的电影文件获取豆瓣信息
     * @param movieInfos 目录下电影文件list
     * @param searchApikey 豆瓣搜索专用 apikey 从数据库获取
     * @param idApikey 通过豆瓣id请求api apikey 从数据库获取
     * @return 返回状态码信息实体 {@link BaseResult}
     */
    BaseResult getDoubanMovieInfos(List<MovieInfo> movieInfos, String searchApikey, String idApikey);

    /**
     * 通过遍历的电视剧文件获取豆瓣信息
     * @param seriesInfos 目录下电视剧文件list
     * @param searchApikey 豆瓣搜索专用 apikey 从数据库获取
     * @param idApikey 通过豆瓣id请求api apikey 从数据库获取
     * @return 返回状态码信息实体 {@link BaseResult}
     */
    BaseResult getDoubanSeriesInfos(List<SeriesInfo> seriesInfos, String searchApikey, String idApikey);


}
