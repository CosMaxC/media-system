package com.cosmax.media.system.business.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cosmax.media.system.business.business.MediaLibraryBusiness;
import com.cosmax.media.system.business.dto.MediaLibraryDto;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.*;
import com.cosmax.media.system.commons.domain.douban.*;
import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import com.cosmax.media.system.commons.response.DoubanResponse;
import com.cosmax.media.system.commons.response.MovieResponse;
import com.cosmax.media.system.commons.response.SeriesResponse;
import com.cosmax.media.system.commons.utils.FileUtils;
import com.cosmax.media.system.commons.utils.LoggerUtils;
import com.cosmax.media.system.commons.utils.MediaFileUtils;
import com.cosmax.media.system.commons.validation.ValuesValidation;
import com.cosmax.media.system.db.api.*;
import com.cosmax.media.system.douban.api.DouBanRequestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @program: media-system
 * @description: 媒体库调用实现
 * @author: Cosmax
 * @create: 2020/03/02 14:03
 */

@Slf4j
@Service
public class MediaLibraryBusinessImpl implements MediaLibraryBusiness {

    @Value("${douban.apikey.search}")
    public String searchApikey;

    @Value("${douban.apikey.custom}")
    public String customApikey;

    @Autowired
    private MediaLibraryService mediaLibraryService;

    @Autowired
    private MediaLibraryScheduleService mediaLibraryScheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeriesInfoService seriesInfoService;

    @Autowired
    private DoubanInfoService doubanInfoService;

    @Autowired
    private SeriesEpisodeService seriesEpisodeService;

    @Autowired
    private MediaDoubanScheduleService mediaDoubanScheduleService;

    @Resource
    private FileAlterationMonitor monitor;

    @Autowired
    private MediaVideoDoubanService mediaVideoDoubanService;

    @Autowired
    private DouBanRequestService douBanRequestService;

    @Autowired
    private DoubanStarService doubanStarService;

    @Autowired
    private DoubanDirectorService doubanDirectorService;

    @Autowired
    private DoubanWriterService doubanWriterService;

    @Autowired
    private StarService starService;

    @Transactional
    @Override
    public BaseResult insert(MediaLibrary mediaLibrary) {
        BaseResult baseResult = new ValuesValidation<MediaLibrary>().checkIsNull(mediaLibrary, "name", "directory", "type");
        if (baseResult.getCode() != BaseResult.STATUS_SUCCESS){
            return baseResult;
        }
        long id = System.currentTimeMillis();
        mediaLibrary.setId(id);

        // 保存到媒体库
        boolean save = mediaLibraryService.save(mediaLibrary);
        if (!save){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "插入媒体库失败", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("添加媒体库失败！");
        }
        // 保存到自动任务表
        MediaLibrarySchedule mediaLibrarySchedule = new MediaLibrarySchedule();
        mediaLibrarySchedule.setLibraryId(id);
        mediaLibrarySchedule.setState(MediaLibrarySchedule.SAVE_STATE);
        mediaLibrarySchedule.setIsSuccess(SuccessEnum.UNSUCCESS.getId());
        boolean scheduleSave = mediaLibraryScheduleService.save(mediaLibrarySchedule);
        if (!scheduleSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "插入媒体库失败", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("插入媒体库任务表失败！");
        }

        // 保存到豆瓣任务表
        MediaDoubanSchedule mediaDoubanSchedule = new MediaDoubanSchedule();
        mediaDoubanSchedule.setMediaId(id);
        mediaDoubanSchedule.setState(SuccessEnum.UNSUCCESS.getId());
        boolean ediaDoubanScheduleSave = mediaDoubanScheduleService.save(mediaDoubanSchedule);
        if (!ediaDoubanScheduleSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "插入豆瓣任务表失败", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("插入豆瓣任务表失败！");
        }
        return BaseResult.success("插入成功！", id);
    }

    @Transactional
    @Override
    public BaseResult update(MediaLibraryDto dto) {

        BaseResult baseResult = new ValuesValidation<MediaLibraryDto>().checkIsNull(dto, "id", "directory");
        if (baseResult.getCode() != BaseResult.STATUS_SUCCESS){
            return baseResult;
        }

        QueryWrapper<MediaLibrary> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MediaLibrary::getId, dto.getId());
        MediaLibrary one = mediaLibraryService.getOne(wrapper);
        if (one == null || one.getId() == null || one.getId() <= 0){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "媒体库[" + dto.getId() + "]不存在！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
            return BaseResult.fail("媒体库[" + dto.getId() + "]不存在！");
        }
        if(one.getDirectory().equals(dto.getDirectory())){
            return BaseResult.success("目录未更改，不需要保存！");
        }
        // 更新媒体库目录
        one.setDirectory(dto.getDirectory());
        boolean update = mediaLibraryService.updateById(one);
        if (!update){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "更新媒体库失败", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("更新媒体库失败！");
        }
        // 更新任务表
        MediaLibrarySchedule mediaLibrarySchedule = new MediaLibrarySchedule();
        mediaLibrarySchedule.setLibraryId(dto.getId());
        mediaLibrarySchedule.setState(MediaLibrarySchedule.SAVE_STATE);
        mediaLibrarySchedule.setIsSuccess(SuccessEnum.UNSUCCESS.getId());
        boolean scheduleUpdate = mediaLibraryScheduleService.update(mediaLibrarySchedule, new UpdateWrapper<MediaLibrarySchedule>()
                .lambda()
                .eq(MediaLibrarySchedule::getLibraryId, mediaLibrarySchedule.getLibraryId()));
        if (!scheduleUpdate){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "更新媒体库任务失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 3);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("更新媒体库任务失败！");
        }

        // 更新豆瓣任务表
        MediaDoubanSchedule mediaDoubanSchedule = new MediaDoubanSchedule();
        mediaDoubanSchedule.setMediaId(dto.getId());
        mediaDoubanSchedule.setState(SuccessEnum.UNSUCCESS.getId());
        boolean mediaDoubanScheduleUpdate = mediaDoubanScheduleService.update(mediaDoubanSchedule, new UpdateWrapper<MediaDoubanSchedule>().lambda().eq(MediaDoubanSchedule::getMediaId, dto.getId()));
        if (!mediaDoubanScheduleUpdate){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "更新豆瓣任务表失败", Thread.currentThread().getStackTrace()[1].getMethodName(), 4);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("更新豆瓣任务表失败！");
        }
        return BaseResult.success("更新媒体库成功！", dto.getId());
    }

    @Transactional
    @Override
    public BaseResult delete(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }

        MediaLibrary one = mediaLibraryService.getOne(new QueryWrapper<MediaLibrary>().lambda().eq(MediaLibrary::getId, id));
        if (one == null || one.getId() == null || one.getId() <= 0){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "媒体库[" + id + "]不存在！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
            return BaseResult.fail("媒体库[" + id + "]不存在！");
        }

        // 删除媒体库
        boolean remove = mediaLibraryService.removeById(id);
        if (!remove){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "删除媒体库失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("删除媒体库失败！");
        }

        // 标记媒体库任务
        // 更新任务表
        MediaLibrarySchedule mediaLibrarySchedule = new MediaLibrarySchedule();
        mediaLibrarySchedule.setLibraryId(id);
        mediaLibrarySchedule.setState(MediaLibrarySchedule.DELETE_STATE);
        mediaLibrarySchedule.setIsSuccess(SuccessEnum.UNSUCCESS.getId());
        boolean scheduleUpdate = mediaLibraryScheduleService.update(mediaLibrarySchedule, new UpdateWrapper<MediaLibrarySchedule>()
                .lambda()
                .eq(MediaLibrarySchedule::getLibraryId, mediaLibrarySchedule.getLibraryId()));
        if (!scheduleUpdate){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "删除媒体库任务失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 3);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("删除媒体库任务失败！");
        }
        // 删除豆瓣任务表
        boolean mediaDoubanScheduleRemove = mediaDoubanScheduleService.removeById(id);
        if (!mediaDoubanScheduleRemove){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "删除豆瓣任务表失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 4);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BaseResult.fail("删除豆瓣任务表失败！");
        }
        // 删除观察者
        deleteObservers(one);
        return BaseResult.success("删除媒体库成功！", id);
    }

    /**
     * 删除观察者
     * @param one 媒体库实体
     */
    private void deleteObservers(MediaLibrary one) {
        Iterable<FileAlterationObserver> observers = monitor.getObservers();
        File file = new File(one.getDirectory());
        for (FileAlterationObserver observer : observers) {
            try {
                String canonicalPath = observer.getDirectory().getCanonicalPath();
                if (canonicalPath.equals(file.getCanonicalPath())){
                    monitor.removeObserver(observer);
                }
            } catch (IOException e) {
                log.info("该文件夹不存在: " + e);

            }
        }
    }

    @Override
    public BaseResult queryAll() {
        List<MediaLibrary> list = mediaLibraryService.list();
        return BaseResult.success("查询成功！", list);
    }

    @Override
    public BaseResult queryInfoById(Long id) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        MediaLibrary one = mediaLibraryService.getOne(new QueryWrapper<MediaLibrary>().lambda().eq(MediaLibrary::getId, id));
        if (one == null || one.getId() == null || one.getId() <= 0){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.ERROR_LOGGER, "媒体库[" + id + "]不存在！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
            return BaseResult.fail("媒体库[" + id + "]不存在！");
        }
        response.put("libraryId", id);
        response.put("libraryName", one.getName());
        response.put("libraryType", one.getType());
        // 获取剧集或电影信息
        List<SeriesAndMovieQueryResponse> seriesAndMovieQueryResponses = mediaLibraryService.getSeriesAndMovieQueryResponseById(id);
        response.put("videos", seriesAndMovieQueryResponses);
        return BaseResult.success("查询成功！", response);
    }

    @Override
    public BaseResult queryMovieAllInfos(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        MovieResponse movieResponse = new MovieResponse();
        //获取电影目录信息
        Movie one = movieService.getOne(new QueryWrapper<Movie>().lambda().eq(Movie::getMovieId, id));
        movieResponse.setMovieInfo(one);
        DoubanResponse doubanResponse = doubanInfoService.getDoubanResponseByVideosId(id);
        movieResponse.setDoubanResponse(doubanResponse);
        return BaseResult.success("查询成功!", movieResponse);
    }

    @Override
    public BaseResult querySeriesAllInfos(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        SeriesResponse seriesResponse = new SeriesResponse();
        // 剧集信息
        SeriesInfo one = seriesInfoService.getOne(new QueryWrapper<SeriesInfo>().lambda().eq(SeriesInfo::getSeriesId, id));
        seriesResponse.setSeriesInfo(one);
        // 集信息
        List<SeriesEpisode> seriesEpisodes = seriesEpisodeService.list(new QueryWrapper<SeriesEpisode>().lambda().eq(SeriesEpisode::getSeriesId, id));
        seriesResponse.setSeriesEpisodes(seriesEpisodes);
        // 豆瓣信息
        DoubanResponse doubanResponse = doubanInfoService.getDoubanResponseByVideosId(id);
        seriesResponse.setDoubanResponse(doubanResponse);
        return BaseResult.success("查询成功!", seriesResponse);
    }

    @Transactional
    @Override
    public BaseResult insertVideoToMediaLibrary(MediaLibrary mediaLibrary, String filePath) {
        BaseResult doubanInfo = null; // 调用api获取豆瓣信息返回实体
        List<DoubanVideoInfo> infos = new ArrayList<>(); // api返回豆瓣电影信息
        List<Star> stars = new ArrayList<>(); // api返回演员信息

        File file = new File(filePath);
        // 判断媒体库类型
        if (mediaLibrary.getType().equals(SubTypeEnum.MOVIE.getId())){
            // 电影类型
            // 获取电影信息
            MovieInfo movieInfo = MediaFileUtils.getMovieInfoByFile(file);
            Movie movieInfosByLibraryIdAndMovieName = movieService.getMovieInfosByLibraryIdAndMovieName(mediaLibrary.getId(), movieInfo.getName());
            if (movieInfosByLibraryIdAndMovieName != null){
                return BaseResult.fail(mediaLibrary + "---" + filePath + "文件已存在！");
            }
            // 硬链接--已废除
//            String fileName = movieInfo.getName() + "." + movieInfo.getSuffix();
//            String link = FileUtils.createLink(movieInfo.getPath(), mediaLibrary.getId(), fileName);
//            if (StringUtils.isBlank(link)){
//                log.warn(movieInfo.getName() + "硬链接失败！");
//                return BaseResult.fail(mediaLibrary.getId() + "----" + movieInfo.getName() + "硬链接失败！");
//            }
//            movieInfo.setLinkPath(link);
            // 写入数据库
            // 写入media_video_douban和movie表
            long movieId = System.currentTimeMillis();

            // 封装电影表
            Movie movie = new Movie();
            movie.setMovieId(movieId);
            movie.setMovieLocation(movieInfo.getPath());
            movie.setMovieName(movieInfo.getName());
            boolean save = movieService.save(movie);
            if (!save){
                LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "电影[" + movieInfo.getName() +"]插入失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail("电影[" + movieInfo.getName() +"]插入失败!");
            }

            // 封装连接表
            MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
            mediaVideoDouban.setMediaId(mediaLibrary.getId());
            mediaVideoDouban.setItemId(movieId);

            // 写入连接表
            boolean mediaVideoDoubanSave = mediaVideoDoubanService.save(mediaVideoDouban);
            if (!mediaVideoDoubanSave){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "视频["+mediaLibrary.getId()+ "---" + movieId +"]保存media_video_douban失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
                return BaseResult.fail("视频["+mediaLibrary.getId()+ "---" + movieId +"]保存media_video_douban失败!");
            }

            // 搜索豆瓣信息返回豆瓣响应实体
            List<MovieInfo> list = new ArrayList<>();
            list.add(movieInfo);
            doubanInfo = douBanRequestService.getDoubanMovieInfos(list, searchApikey, customApikey);

        }
        if (mediaLibrary.getType().equals(SubTypeEnum.TV.getId())){
            // 剧集类型
            com.cosmax.media.system.commons.domain.SeriesInfo seriesInfoByFile = MediaFileUtils.getSeriesInfoByFile(file);
            if (seriesInfoByFile == null){
                // 获取不了则返回
                log.warn(filePath + "无法获取信息！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail(filePath + "无法获取信息！");
            }
            // 查看数据库是否有剧集
            String seriesName = seriesInfoByFile.getName() + " 第" + seriesInfoByFile.getSeason() + "季";
            SeriesInfo one = seriesInfoService.getOne(new QueryWrapper<SeriesInfo>().lambda().eq(SeriesInfo::getSeriesName, seriesName));
            boolean isSearch = false; // 用于判断是否需要获取豆瓣信息，如果数据库已存在，则不需要,状态为false;否则需要，状态改为true
            if (one == null){
                // 新剧集, 数据库不存在
                // 插入SeriesInfo表
                // 封装 com.cosmax.media.system.commons.domain.douban.SeriesInfo
                SeriesInfo seriesInfoSql = new SeriesInfo();
                Long seriesId = System.currentTimeMillis();
                seriesInfoSql.setSeriesId(seriesId);
                seriesInfoSql.setSeriesName(seriesName);
                seriesInfoSql.setSeriesLocation(file.getParent());
                boolean save = seriesInfoService.save(seriesInfoSql);
                if (!save){
                    log.warn(filePath + "插入seriesInfo失败！");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return BaseResult.fail(filePath + "插入seriesInfo失败！");
                }
                one = seriesInfoSql;
                isSearch = true;

            }
            // 把集数写入数据库
            // 判断集在数据库是否已存在
            EpisodeInfo episodeInfoByFile = MediaFileUtils.getEpisodeInfoByFile(file);
            if (episodeInfoByFile == null){
                // 获取不了则返回
                log.warn(filePath + "无法获取集信息！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail(filePath + "无法获取集信息！");
            }
            SeriesEpisode getInfo = seriesEpisodeService.getEpisodesInfosBySeriesNameAndEpisode(mediaLibrary.getId(), seriesName, episodeInfoByFile.getEpisode());
            if (getInfo != null){
                return BaseResult.fail(mediaLibrary + "---" + filePath + "文件已存在！");
            }
            // 硬链接-- 已废除
//            String fileName = seriesInfoByFile.getName() + "." + MediaFileUtils.getSeasonEpisode(seriesInfoByFile.getSeason(), episodeInfoByFile.getEpisode()) + "." + episodeInfoByFile.getSuffix();
//            String link = FileUtils.createLink(episodeInfoByFile.getPath(), mediaLibrary.getId(), fileName);
//            if (StringUtils.isBlank(link)){
//                log.warn(filePath + "创建硬链接失败！");
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return BaseResult.fail(filePath + "创建硬链接失败！");
//            }
//            episodeInfoByFile.setLinkPath(link);

            // 封装SeriesEpisode表字段
            SeriesEpisode seriesEpisode = new SeriesEpisode();
            seriesEpisode.setSeriesId(one.getSeriesId());
            seriesEpisode.setSeason(seriesInfoByFile.getSeason());
            seriesEpisode.setEpisode(episodeInfoByFile.getEpisode());
            seriesEpisode.setEpisodeLocation(episodeInfoByFile.getPath());
            boolean save = seriesEpisodeService.save(seriesEpisode);
            if (!save){
                log.warn(filePath + "插入SeriesEpisode失败！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail(filePath + "插入SeriesEpisode失败！");
            }
            // 插入豆瓣信息
            if (isSearch){
                ArrayList<com.cosmax.media.system.commons.domain.SeriesInfo> seriesInfos = new ArrayList<>();
                seriesInfos.add(seriesInfoByFile);
                doubanInfo = douBanRequestService.getDoubanSeriesInfos(seriesInfos, searchApikey, customApikey);
            }else {
                // 如果不用搜索，则剧集豆瓣信息已有，将集数导入数据库即可
                return BaseResult.success("添加数据库成功！");
            }
        }
        // 豆瓣信息存入数据库
        if (doubanInfo != null){
            // 不为空则表示发起过请求
            if(doubanInfo.getCode() != BaseResult.STATUS_SUCCESS){
                // 获取豆瓣信息失败 直接跳出
                log.warn(filePath + "导入成功，但找不到豆瓣信息！");
                return BaseResult.success(filePath + "导入成功，但找不到豆瓣信息！");
            }
            // 获取到豆瓣信息
            Object data = doubanInfo.getData();
            if (data != null){
                String dataStr = JSON.toJSONString(data);
                JSONObject dataToJson = JSONObject.parseObject(dataStr);
                infos = JSONObject.parseArray(dataToJson.getString("infos"), DoubanVideoInfo.class);
                stars = JSONObject.parseArray(dataToJson.getString("stars"), Star.class);
            }
            // 存入数据库
            dataToDataBase(infos, stars);
            // 绑定 影视和豆瓣
            linkVideoWithDouban(infos, mediaLibrary.getType(), mediaLibrary.getId());
            return BaseResult.success("导入成功，豆瓣信息已更新！");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        log.warn("发生未知错误！");
        return BaseResult.fail("发生未知错误！");
    }

    @Transactional
    @Override
    public BaseResult deleteVideoToMediaLibrary(MediaLibrary mediaLibrary, String filePath) {
        /* 删除文件
         * 电影：删除链接表，删除link，删除电影info表
         * 剧集：删除集，删除link
         */
        File file = new File(filePath);
//        String link = "";
        // 判断媒体库类型
        Integer type = mediaLibrary.getType();
        if (type.equals(SubTypeEnum.MOVIE.getId())){
            // 电影
            // 获取filepath信息
            MovieInfo movieInfoByFile = MediaFileUtils.getMovieInfoByFile(file);
            Movie movie = movieService.getMovieInfosByLibraryIdAndMovieName(mediaLibrary.getId(), movieInfoByFile.getName());
            if (movie == null){
                log.warn("该电影在数据库中不存在！");
                return BaseResult.success("该电影在数据库中不存在！");
            }
//            link = movie.getMovieLocation();
            // 删除链接表
            boolean mediaVideoDoubanRemove = mediaVideoDoubanService.remove(new QueryWrapper<MediaVideoDouban>()
                    .lambda()
                    .eq(MediaVideoDouban::getMediaId, mediaLibrary.getId())
                    .eq(MediaVideoDouban::getItemId, movie.getMovieId()));
            if (!mediaVideoDoubanRemove){
                // 删除失败
                log.warn("["+mediaLibrary.getId()+"---"+movie.getMovieId()+"]在链接表删除失败！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail("["+mediaLibrary.getId()+"---"+movie.getMovieId()+"]在链接表删除失败！");
            }
            // 删除info表
            boolean movieServiceRemove = movieService.remove(new QueryWrapper<Movie>().lambda().eq(Movie::getMovieId, movie.getMovieId()));
            if (!movieServiceRemove){
                // 删除失败
                log.warn("["+movie.getMovieId()+"]在movieService表删除失败！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail("["+movie.getMovieId()+"]在movieService表删除失败！");
            }
        }
        if (type.equals(SubTypeEnum.TV.getId())){
            // 获取总体信息
            com.cosmax.media.system.commons.domain.SeriesInfo seriesInfoByFile = MediaFileUtils.getSeriesInfoByFile(file);
            if (seriesInfoByFile == null){
                // 获取不了则返回
                log.warn(filePath + "无法获取信息！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail(filePath + "无法获取信息！");
            }
            String seriesName = seriesInfoByFile.getName() + " 第" + seriesInfoByFile.getSeason() + "季";
            // 获取集数
            EpisodeInfo episodeInfoByFile = MediaFileUtils.getEpisodeInfoByFile(file);
            if (episodeInfoByFile == null){
                // 获取不了则返回
                log.warn(filePath + "无法获取信息！");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BaseResult.fail(filePath + "无法获取信息！");
            }
            // 获取集链接
            SeriesEpisode seriesEpisode = seriesEpisodeService.getEpisodesInfosBySeriesNameAndEpisode(mediaLibrary.getId(), seriesName, episodeInfoByFile.getEpisode());
            if (seriesEpisode == null || StringUtils.isBlank(seriesEpisode.getEpisodeLocation())){
                return BaseResult.fail(filePath + "找不到集路径！");
            }
//            link = seriesEpisode.getEpisodeLocation();
            seriesEpisodeService.deleteSeriesEpisodeByMediaIdAndSeriesIdAndEpisode(mediaLibrary.getId(), seriesName, episodeInfoByFile.getEpisode());
        }
        // 删除link--硬链接已废除
//        boolean flag = FileUtils.deleteQuietly(new File(FileUtils.getJarFilePath() + "/" + link));
//        if (flag){
//            return BaseResult.success("删除硬链接成功！");
//        }
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        return BaseResult.fail("删除硬链接失败！");
        return BaseResult.success(mediaLibrary.getId() +"---"+ filePath + "---" +"删除成功！");
    }

    @Override
    public BaseResult getMediaBaseInfo(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        MediaLibrary one = mediaLibraryService.getOne(new QueryWrapper<MediaLibrary>().lambda().eq(MediaLibrary::getId, id));
        if (one == null){
            return BaseResult.fail("id[" + id +"]媒体库不存在！");
        }
        return BaseResult.success("查询成功！", one);
    }

    @Transactional
    @Override
    public BaseResult refreshMedia(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        // 查询媒体库信息
        MediaLibrary one = mediaLibraryService.getOne(new QueryWrapper<MediaLibrary>().lambda().eq(MediaLibrary::getId, id));
        if (one == null) {
            return BaseResult.fail("该媒体库["+ id +"]不存在");
        }
        // 删除关联表，剧集表，电影表指定id的关联信息
        Boolean flag = mediaLibraryService.deleteMediaVideosAndDoubanInfosById(id);
        if (!flag) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("MediaLibraryBusinessImpl--refreshMedia--删除媒体库["+ id +"]删除关联表，剧集表，电影表指定id的关联信息失败!");
            return BaseResult.fail("刷新媒体库["+ id +"]失败!");
        }
        // 修改媒体库任务表
        MediaLibrarySchedule mediaLibrarySchedule = new MediaLibrarySchedule();
        mediaLibrarySchedule.setLibraryId(id);
        mediaLibrarySchedule.setState(MediaLibrarySchedule.SAVE_STATE);
        mediaLibrarySchedule.setIsSuccess(SuccessEnum.UNSUCCESS.getId());
        boolean updateMediaLibrarySchedule = mediaLibraryScheduleService.updateById(mediaLibrarySchedule);
        if (!updateMediaLibrarySchedule) {
            // 修改不成功回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("MediaLibraryBusinessImpl--refreshMedia--修改媒体库["+ id +"]任务表失败!");
            return BaseResult.fail("刷新媒体库["+ id +"]失败!");
        }
        // 修改豆瓣任务表
        MediaDoubanSchedule mediaDoubanSchedule = new MediaDoubanSchedule();
        mediaDoubanSchedule.setMediaId(id);
        mediaDoubanSchedule.setState(SuccessEnum.UNSUCCESS.getId());
        boolean updateMediaDoubanSchedule = mediaDoubanScheduleService.updateById(mediaDoubanSchedule);
        if (!updateMediaDoubanSchedule){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("MediaLibraryBusinessImpl--refreshMedia--修改媒体库["+ id +"]豆瓣任务表失败!");
            return BaseResult.fail("刷新媒体库["+ id +"]失败!");
        }
        return BaseResult.success("刷新成功！");
    }

    @Override
    public BaseResult refreshDouban(Long id) {
        if (id == null || id <= 0){
            return BaseResult.fail("id不能为空！");
        }
        // 查询媒体库信息
        MediaLibrary one = mediaLibraryService.getOne(new QueryWrapper<MediaLibrary>().lambda().eq(MediaLibrary::getId, id));
        if (one == null) {
            return BaseResult.fail("该媒体库["+ id +"]不存在");
        }
        MediaDoubanSchedule mediaDoubanSchedule = new MediaDoubanSchedule();
        mediaDoubanSchedule.setMediaId(id);
        mediaDoubanSchedule.setState(SuccessEnum.UNSUCCESS.getId());
        boolean flag = mediaDoubanScheduleService.updateById(mediaDoubanSchedule);
        if (!flag){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("MediaLibraryBusinessImpl--refreshDouban--修改媒体库["+ id +"]豆瓣任务表失败!");
            return BaseResult.fail("刷新媒体库["+ id +"]豆瓣信息失败!");
        }
        return BaseResult.success("刷新成功！");
    }

    /**
     * 存入数据库
     * @param infos 豆瓣视频信息
     * @param stars 演员信息
     */
    private void dataToDataBase(List<DoubanVideoInfo> infos, List<Star> stars) {
        List<DoubanInfo> doubanInfos = new ArrayList<>();
        List<DoubanDirector> doubanDirectors = new ArrayList<>();
        List<DoubanWriter> doubanWriters = new ArrayList<>();
        List<DoubanStar> doubanStars = new ArrayList<>();
        List<Long> doubanIds = new ArrayList<>();

        for (DoubanVideoInfo info : infos) {
            DoubanInfo doubanInfo = info.getDoubanInfo();
            doubanInfos.add(doubanInfo);
            doubanIds.add(doubanInfo.getDoubanId()); // 用于删除豆瓣导演表等的id

            // 获取导演
            List<Long> directorList = info.getDirectorList();
            for (Long directorId : directorList) {
                DoubanDirector doubanDirector = new DoubanDirector();
                doubanDirector.setDoubanId(doubanInfo.getDoubanId());
                doubanDirector.setDirectorId(directorId);
                doubanDirectors.add(doubanDirector);
            }

            // 获取编剧
            List<Long> writerList = info.getWriterList();
            for (Long writerId : writerList) {
                DoubanWriter doubanWriter = new DoubanWriter();
                doubanWriter.setDoubanId(doubanInfo.getDoubanId());
                doubanWriter.setWriterId(writerId);
                doubanWriters.add(doubanWriter);
            }

            // 获取演员
            List<Long> actorList = info.getActorList();
            for (Long actorId : actorList) {
                DoubanStar doubanStar = new DoubanStar();
                doubanStar.setDoubanId(doubanInfo.getDoubanId());
                doubanStar.setStarId(actorId);
                doubanStars.add(doubanStar);
            }
        }

        // 写入数据库

        // 豆瓣信息
        boolean doubanInfoSave = doubanInfoService.saveOrUpdateBatch(doubanInfos);
        if (!doubanInfoSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣信息保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣信息保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }

        // 豆瓣导演
        doubanDirectorService.removeByIds(doubanIds);
        boolean doubanDirectorSave = doubanDirectorService.saveBatch(doubanDirectors);
        if (!doubanDirectorSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣导演保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣导演保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }

        // 豆瓣编剧
        doubanWriterService.removeByIds(doubanIds);
        boolean doubanWritersSave = doubanWriterService.saveBatch(doubanWriters);
        if (!doubanWritersSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣编剧保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣编剧保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }

        // 豆瓣演员
        doubanStarService.removeByIds(doubanIds);
        boolean doubanStarsSave = doubanStarService.saveOrUpdateBatch(doubanStars);
        if (!doubanStarsSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣演员保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣演员保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }

        boolean starsSave = starService.saveOrUpdateBatch(stars);
        if (!starsSave){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣演员信息保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣演员信息保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }
    }
    /**
     * 绑定 影视和豆瓣
     * @param infos 豆瓣信息
     * @param type 视频类型： 0|电影，1|剧集
     * @param mediaId 媒体库id
     */
    private void linkVideoWithDouban(List<DoubanVideoInfo> infos, Integer type, Long mediaId) {
        List<MediaVideoDouban> mediaVideoDoubans = new ArrayList<>();
        for (DoubanVideoInfo info : infos) {
            String videoName = info.getVideoName();
            if (type.equals(SubTypeEnum.MOVIE.getId())){
                Movie one = movieService.getOne(new QueryWrapper<Movie>().lambda().eq(Movie::getMovieName, videoName));
                if (one != null){
                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
                    mediaVideoDouban.setMediaId(mediaId);
                    mediaVideoDouban.setItemId(one.getMovieId());
                    mediaVideoDouban.setDoubanId(info.getDoubanInfo().getDoubanId());
                    mediaVideoDoubans.add(mediaVideoDouban);
                }
            }
            if (type.equals(SubTypeEnum.TV.getId())){
                com.cosmax.media.system.commons.domain.douban.SeriesInfo seriesInfo = seriesInfoService.getOne(new QueryWrapper<com.cosmax.media.system.commons.domain.douban.SeriesInfo>().lambda().eq(com.cosmax.media.system.commons.domain.douban.SeriesInfo::getSeriesName, videoName));
                if (seriesInfo != null){
                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
                    mediaVideoDouban.setMediaId(mediaId);
                    mediaVideoDouban.setItemId(seriesInfo.getSeriesId());
                    mediaVideoDouban.setDoubanId(info.getDoubanInfo().getDoubanId());
                    mediaVideoDoubans.add(mediaVideoDouban);
                }
            }
        }
        // 删除mediaVideoDouban表中媒体库信息重新绑
        mediaVideoDoubanService.removeById(mediaId);
        boolean flag = mediaVideoDoubanService.saveBatch(mediaVideoDoubans);
        if (!flag){
            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "绑定媒体库豆瓣视频失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }else {
            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "绑定媒体库豆瓣视频成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
        }
    }

}
