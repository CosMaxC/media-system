package com.cosmax.media.system.douban.provider.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.DoubanVideoInfo;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.commons.domain.SeriesInfo;

import com.cosmax.media.system.commons.domain.SubTypeEnum;
import com.cosmax.media.system.commons.domain.douban.Star;
import com.cosmax.media.system.douban.api.DouBanRequestService;
import com.cosmax.media.system.douban.provider.biz.DoubanRequestBiz;
import com.cosmax.media.system.douban.provider.constants.URLConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: media-system
 * @description: 豆瓣api实现
 * @author: Cosmax
 * @create: 2020/02/05 11:23
 */

@Slf4j
@Service
public class DouBanRequestServiceImpl implements DouBanRequestService{

    @Resource
    private DoubanRequestBiz doubanRequestBiz;

    @Override
    public BaseResult getDoubanMovieInfos(List<MovieInfo> movieInfos, String searchApikey, String idApikey) {

        // 所有剧集演员组成的ids，用于一次性下载图片
        List<Long> starIdList = new ArrayList<>();

        if (movieInfos == null || movieInfos.size() <= 0){
            return BaseResult.fail("剧集list为空！");
        }
        if (StringUtils.isBlank(searchApikey)){
            return BaseResult.fail("searchApikey为空！");
        }
        if (StringUtils.isBlank(idApikey)){
            return BaseResult.fail("idApikey为空！");
        }
        log.info("正在获取豆瓣电影实体。。。");
        List<DoubanVideoInfo> doubanVideoInfos = new ArrayList<>();

        for (MovieInfo movieInfo : movieInfos) {
            JSONObject searchJson = getSearchDoubanRequest(movieInfo.getName() + " " + movieInfo.getYear(), searchApikey);
            // 校验响应是否正常 有code不正常
            if (searchJson == null || searchJson.containsKey("code")){
                continue;
            }
            // 封装豆瓣实体
            DoubanVideoInfo doubanVideoInfo = getDoubanInfoByDoubanJson(searchJson, idApikey, SubTypeEnum.MOVIE.getId());
            if (doubanVideoInfo != null) {
                doubanVideoInfo.setVideoName(movieInfo.getName());

                // 获得所有演员id列表，排除重复项
                starIdList = Stream
                        .of(doubanVideoInfo.getActorList(),
                                doubanVideoInfo.getDirectorList(),
                                doubanVideoInfo.getWriterList(),
                                starIdList)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
                doubanVideoInfos.add(doubanVideoInfo);
            }
        }
        log.info("获取豆瓣电影实体成功！");
        log.info("正在下载豆瓣演员图片。。。");

        // 下载图片
        List<Star> list = new ArrayList<>();
        starIdList.forEach(id-> list.add(doubanRequestBiz.getStarByStarId(id, idApikey)));
        log.info("下载豆瓣演员图片完成！");
        Map<String, Object> response = new HashMap<>();
        response.put("infos", doubanVideoInfos);
        response.put("stars", list);

        return BaseResult.success("成功！", response);
    }

    @Override
    public BaseResult getDoubanSeriesInfos(List<SeriesInfo> seriesInfos, String searchApikey, String idApikey) {

        // 所有剧集演员组成的ids，用于一次性下载图片
        List<Long> starIdList = new ArrayList<>();

        if (seriesInfos == null || seriesInfos.size() <= 0){
            return BaseResult.fail("剧集list为空！");
        }
        if (StringUtils.isBlank(searchApikey)){
            return BaseResult.fail("searchApikey为空！");
        }
        if (StringUtils.isBlank(idApikey)){
            return BaseResult.fail("idApikey为空！");
        }
        log.info("正在获取豆瓣剧集实体。。。");
        List<DoubanVideoInfo> doubanVideoInfos = new ArrayList<>();
        // 遍历seriesInfos获取剧集信息
        for (SeriesInfo seriesInfo : seriesInfos) {
            // 剧集搜索
            String name = seriesInfo.getName();
            String season = "第" + seriesInfo.getSeason() + "季";
            String searchKey = name + " " + season;
            JSONObject searchJson = getSearchDoubanRequest(searchKey, searchApikey);
            // 校验响应是否正常 有code不正常
            if (searchJson == null || searchJson.containsKey("code")){
                continue;
            }
            // 封装豆瓣实体
            DoubanVideoInfo doubanVideoInfo = getDoubanInfoByDoubanJson(searchJson, idApikey, SubTypeEnum.TV.getId());
            if (doubanVideoInfo != null) {
                doubanVideoInfo.setVideoName(searchKey);
                // 获得所有演员id列表，排除重复项
                starIdList = Stream
                        .of(doubanVideoInfo.getActorList(),
                                doubanVideoInfo.getDirectorList(),
                                doubanVideoInfo.getWriterList(),
                                starIdList)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
                doubanVideoInfos.add(doubanVideoInfo);
            }
        }
        log.info("获取豆瓣电影实体成功！");
        log.info("正在下载豆瓣演员图片。。。");
        // 下载图片
        List<Star> list = new ArrayList<>();
        starIdList.forEach(id-> list.add(doubanRequestBiz.getStarByStarId(id, idApikey)));
        log.info("下载豆瓣演员图片完成！");
        Map<String, Object> response = new HashMap<>();
        response.put("infos", doubanVideoInfos);
        response.put("stars", list);
        return BaseResult.success("成功！", response);
    }

    /**
     * 通过搜索的豆瓣信息封装实体
     * @param searchJson 豆瓣搜索返回json
     * @param apikey 密钥
     * @param subType 条目类型：0为电影，1为电视剧
     * @return 豆瓣信息实体 {@link DoubanVideoInfo}
     */
    private DoubanVideoInfo getDoubanInfoByDoubanJson(JSONObject searchJson, String apikey, int subType) {

        if (searchJson.getLong("total") <= 0){
            return null;
        }
        // 获取条目类型
        String type = null;
        if (subType == 0){
            type = "movie";
        }
        if (subType == 1){
            type = "tv";
        }
        if (type == null){
            return null;
        }
        JSONArray subjects = searchJson.getJSONArray("subjects");
        JSONObject subject = null;
        for (int i = 0; i < subjects.size(); i++) {
        // 获取第一个相关subType的subject
            JSONObject checkSubject = subjects.getJSONObject(i);
            if (type.equals(checkSubject.getString("subtype"))){
                subject = checkSubject;
                break;
            }
        }
        // 如果找不到，即豆瓣不存在返回
        if (subject == null){
            return null;
        }

        return doubanRequestBiz.getDoubanInfoByDoubanId(subject.getLong("id"), apikey);
    }

    /**
     * 获取豆瓣搜索功能api
     * @param searchKey 关键字
     * @param apikey 密钥
     * @return JSONObject 搜索结果
     */
    private JSONObject getSearchDoubanRequest(String searchKey, String apikey) {

        if (StringUtils.isBlank(searchKey) || StringUtils.isBlank(apikey)){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        // url编码
        map.put("q", searchKey);
        map.put("apikey", apikey);
        WebClient webClient = WebClient.create();

        return webClient.get().uri(URLConstant.MOVIE_SEARCH + "?q={q}&apikey={apikey}", map)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> null)
                .bodyToMono(JSONObject.class)
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .block();

    }

}
