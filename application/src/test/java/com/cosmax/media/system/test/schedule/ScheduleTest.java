//package com.cosmax.media.system.test.schedule;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.cosmax.media.system.MediaSystemApplication;
//import com.cosmax.media.system.business.utils.FileListenerMonitorUtils;
//import com.cosmax.media.system.commons.BaseResult;
//import com.cosmax.media.system.commons.domain.*;
//import com.cosmax.media.system.commons.domain.SeriesInfo;
//import com.cosmax.media.system.commons.domain.douban.*;
//import com.cosmax.media.system.commons.utils.FileUtils;
//import com.cosmax.media.system.commons.utils.LoggerUtils;
//import com.cosmax.media.system.db.api.*;
//import com.cosmax.media.system.douban.api.DouBanRequestService;
//import com.cosmax.media.system.file.api.FileService;
//import org.apache.commons.io.monitor.FileAlterationMonitor;
//import org.apache.commons.io.monitor.FileAlterationObserver;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @program: media-system
// * @description:
// * @author: Cosmax
// * @create: 2020/03/06 21:20
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MediaSystemApplication.class)
//public class ScheduleTest {
//
//    private static final String searchApikey = "0dad551ec0f84ed02907ff5c42e8ec70";
//
//    private static final String customApikey = "0b2bdeda43b5688921839c8ecb20399b";
//
//    @Autowired
//    private MediaLibraryService mediaLibraryService;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private MediaLibraryScheduleService scheduleService;
//
//    @Autowired
//    private MovieService movieService;
//
//    @Autowired
//    private SeriesInfoService seriesInfoService;
//
//    @Autowired
//    private SeriesEpisodeService seriesEpisodeService;
//
//    @Autowired
//    private MediaVideoDoubanService mediaVideoDoubanService;
//
//    @Autowired
//    private DouBanRequestService douBanRequestService;
//
//    @Autowired
//    private MediaDoubanScheduleService mediaDoubanScheduleService;
//
//    @Autowired
//    private DoubanInfoService doubanInfoService;
//
//    @Autowired
//    private DoubanDirectorService doubanDirectorService;
//
//    @Autowired
//    private DoubanWriterService doubanWriterService;
//
//    @Autowired
//    private DoubanStarService doubanStarService;
//
//    @Autowired
//    private StarService starService;
//
//    @Resource
//    private FileAlterationMonitor monitor;
//
//    /**
//     * 自动任务：执行遍历文件夹视频生成硬链接，保存数据库
//     */
//
//    @Test
//    public void autoSaveOrUpdateMedia(){
//        // 查询数据库媒体库信息(media_library_schedule表中state为0，is_success为0的媒体库)
//        List<MediaLibrary> mediaLibraries = mediaLibraryService.getScheduleList(MediaLibrarySchedule.SAVE_STATE);
//        if (mediaLibraries == null || mediaLibraries.size() <= 0){
//            //查询结果为空则返回
//            return;
//        }
//        // 媒体库目录遍历文件
//        for (MediaLibrary mediaLibrary : mediaLibraries) {
//            // 删除媒体库目录
//            FileUtils.deleteQuietly(new File(FileUtils.getJarFilePath() + "/" + mediaLibrary.getId()));
//
//            // 删除原来信息
//            initDatabase(mediaLibrary.getId(), mediaLibrary.getType());
//
//            if (mediaLibrary.getType().equals(SubTypeEnum.MOVIE.getId())){
//                // 电影类型
//                // 获取List<MovieInfo>
//                List<MovieInfo> movieInfosFromPath = new ArrayList<>();
//                String directories = mediaLibrary.getDirectory();
//                if (directories.contains("|")){
//                    // 切割目录
//                    String[] split = directories.split("\\|");
//                    for (String s : split) {
//                        // 每个目录添加文件
//                        movieInfosFromPath.addAll(fileService.getMovieInfosFromPath(s));
//                    }
//                }else {
//                        movieInfosFromPath = fileService.getMovieInfosFromPath(directories);
//                }
//
//                for (MovieInfo movieInfo : movieInfosFromPath) {
//                    // 硬链接
//                    String fileName = movieInfo.getName() + "." + movieInfo.getSuffix();
//                    movieInfo.setLinkPath(FileUtils.createLink(movieInfo.getPath(), mediaLibrary.getId(), fileName));
//                    // 写入数据库
//                    // 写入media_video_douban和movie表
//                    long movieId = System.currentTimeMillis();
//
//                    // 封装电影表
//                    Movie movie = new Movie();
//                    movie.setMovieId(movieId);
//                    movie.setMovieLocation(movieInfo.getLinkPath());
//                    movie.setMovieName(movieInfo.getName());
//                    boolean save = movieService.save(movie);
//                    if (!save){
//                        LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "电影[" + movieInfo.getName() +"]插入失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                        continue;
//                    }
//
//                    // 封装连接表
//                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
//                    mediaVideoDouban.setMediaId(mediaLibrary.getId());
//                    mediaVideoDouban.setItemId(movieId);
//
//                    // 写入连接表
//                    boolean mediaVideoDoubanSave = mediaVideoDoubanService.save(mediaVideoDouban);
//                    if (!mediaVideoDoubanSave){
//                        LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "视频["+mediaLibrary.getId()+ "---" + movieId +"]保存media_video_douban失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
//                    }
//                }
//                // 设置任务表is_success标识为1
//                updateScheduleIsSuccess(mediaLibrary.getId(), MediaLibrarySchedule.SAVE_STATE, SuccessEnum.SUCCESS.getId());
//
//            }
//            if (mediaLibrary.getType().equals(SubTypeEnum.TV.getId())){
//                // 剧集类型
//                // 获取List<SeriesInfo>
//                List<SeriesInfo> seriesInfosFormPath = new ArrayList<>();
//                String directories = mediaLibrary.getDirectory();
//                if (directories.contains("|")){
//                    // 切割目录
//                    String[] split = directories.split("\\|");
//                    for (String s : split) {
//                        // 每个目录添加文件
//                        seriesInfosFormPath.addAll(fileService.getSeriesInfosFormPath(s));
//                    }
//                }else {
//                    seriesInfosFormPath = fileService.getSeriesInfosFormPath(directories);
//                }
//                for (SeriesInfo seriesInfo : seriesInfosFormPath) {
//                    List<EpisodeInfo> episodeInfos = seriesInfo.getEpisodeInfos();
//
//                    // 插入SeriesInfo表
//                    // 封装 com.cosmax.media.system.commons.domain.douban.SeriesInfo
//                    com.cosmax.media.system.commons.domain.douban.SeriesInfo seriesInfoSql = new com.cosmax.media.system.commons.domain.douban.SeriesInfo();
//                    Long seriesId = System.currentTimeMillis();
//                    seriesInfoSql.setSeriesId(seriesId);
//                    seriesInfoSql.setSeriesName(seriesInfo.getName() + " 第" + seriesInfo.getSeason() + "季");
//                    seriesInfoSql.setSeriesLocation(seriesInfo.getPath());
//                    boolean save = seriesInfoService.save(seriesInfoSql);
//                    if (!save){
//                        LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "剧集[" + seriesInfoSql.getSeriesName() +"]插入失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 4);
//                        continue;
//                    }
//
//                    // 封装连接表
//                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
//                    mediaVideoDouban.setMediaId(mediaLibrary.getId());
//                    mediaVideoDouban.setItemId(seriesId);
//
//                    // 写入连接表
//                    boolean mediaVideoDoubanSave = mediaVideoDoubanService.save(mediaVideoDouban);
//                    if (!mediaVideoDoubanSave){
//                        LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "视频["+mediaLibrary.getId()+ "---" + seriesId +"]保存media_video_douban失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 5);
//                        continue;
//                    }
//
//                    for (EpisodeInfo episodeInfo : episodeInfos) {
//                        // 硬链接
//                        String fileName = seriesInfo.getName() + "." + getSeasonEpisode(seriesInfo.getSeason(), episodeInfo.getEpisode()) + "." + episodeInfo.getSuffix();
//                        episodeInfo.setLinkPath(FileUtils.createLink(episodeInfo.getPath(), mediaLibrary.getId(), fileName));
//
//                        // 封装SeriesEpisode表字段
//                        SeriesEpisode seriesEpisode = new SeriesEpisode();
//                        seriesEpisode.setSeriesId(seriesId);
//                        seriesEpisode.setSeason(seriesInfo.getSeason());
//                        seriesEpisode.setEpisode(episodeInfo.getEpisode());
//                        seriesEpisode.setEpisodeLocation(episodeInfo.getLinkPath());
//
//                        seriesEpisodeService.save(seriesEpisode);
//                    }
//                }
//                // 设置任务表is_success标识为1
//                updateScheduleIsSuccess(mediaLibrary.getId(), MediaLibrarySchedule.SAVE_STATE, SuccessEnum.SUCCESS.getId());
//            }
//        }
//        LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "任务完成!", Thread.currentThread().getStackTrace()[1].getMethodName(), 6);
//    }
//
//    /**
//     * 自动任务：执行删除任务，删除文件夹，并修改媒体任务状态
//     */
//    @Test
//    public void autoDeleteMedia(){
//
//        // 获取任务表信息
//        // 查询数据库媒体库信息(media_library_schedule表中state为1，is_success为0的媒体库)
//        List<MediaLibrary> mediaLibraries = mediaLibraryService.getScheduleList(MediaLibrarySchedule.DELETE_STATE);
//        if (mediaLibraries == null || mediaLibraries.size() <= 0){
//            return;
//        }
//        for (MediaLibrary mediaLibrary : mediaLibraries) {
//            Long id = mediaLibrary.getId();
//            // 删除表信息
//            if (mediaLibrary.getType().equals(SubTypeEnum.MOVIE.getId())){
//                // movie删除
//                List<Movie> movieInfosByLibraryId = movieService.getMovieInfosByLibraryId(id);
//                boolean movieRemove = movieService.removeByIds(movieInfosByLibraryId);
//                if (!movieRemove){
//                    LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]movies失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                    continue;
//                }
//                LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]movies成功!", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
//                // 删除链接表
//                boolean mediaVideoDoubanRemove = mediaVideoDoubanService.remove(new QueryWrapper<MediaVideoDouban>().lambda().eq(MediaVideoDouban::getMediaId, id));
//                if (!mediaVideoDoubanRemove){
//                    LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]豆瓣表失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                    continue;
//                }
//                LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]豆瓣表成功!", Thread.currentThread().getStackTrace()[1].getMethodName(), 2);
//
//            }
//            if (mediaLibrary.getType().equals(SubTypeEnum.TV.getId())){
//                // series删除
//                List<com.cosmax.media.system.commons.domain.douban.SeriesInfo> seriesInfosByLibraryId = seriesInfoService.getSeriesInfosByLibraryId(id);
//                boolean seriesInfoRemove = seriesInfoService.removeByIds(seriesInfosByLibraryId);
//                if (!seriesInfoRemove){
//                    LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]seriesInfo失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                    continue;
//                }
//                LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]seriesInfo成功!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//
//                // 删除集信息
//                boolean seriesEpisodeRemove = seriesEpisodeService.removeByIds(seriesInfosByLibraryId);
//                if (!seriesEpisodeRemove){
//                    LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]seriesEpisode失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                    continue;
//                }
//                LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]seriesEpisode失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//
//            }
//
//
//            // 删除文件信息
//            boolean flag = FileUtils.deleteQuietly(new File(FileUtils.getJarFilePath() + "/" + id));
//            if (!flag){
//                LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "删除媒体库["+mediaLibrary.getId()+"]硬链接失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//                updateScheduleIsSuccess(id, MediaLibrarySchedule.DELETE_STATE, SuccessEnum.EXCEPTION.getId());
//                continue;
//            }
//            // 修改状态
//            updateScheduleIsSuccess(id, MediaLibrarySchedule.DELETE_STATE, SuccessEnum.SUCCESS.getId());
//        }
//
//    }
//
//    /**
//     * 生成豆瓣信息并保存数据库：逐条操作，抽取时间id最小的媒体库执行
//     */
//    @Test
//    public void autoSaveDoubanInfo(){
//        // 获取媒体库id
//        MediaLibrary mediaLibrary = mediaLibraryService.getLibraryByDoubanCondition();
//        BaseResult doubanInfo = null; // 调用api获取豆瓣信息返回实体
//        List<DoubanVideoInfo> infos = new ArrayList<>(); // api返回豆瓣电影信息
//        List<Star> stars = new ArrayList<>(); // api返回演员信息
//        if (mediaLibrary == null){
//            return;
//        }
//        Integer type = mediaLibrary.getType();
//        if (type.equals(SubTypeEnum.MOVIE.getId())){
//            // 电影
//            List<MovieInfo> movieInfosFromPath = fileService.getMovieInfosFromPath(mediaLibrary.getDirectory());
//            doubanInfo = douBanRequestService.getDoubanMovieInfos(movieInfosFromPath, searchApikey, customApikey);
//
//        }
//        if (type.equals(SubTypeEnum.TV.getId())){
//            // 剧集
//            List<SeriesInfo> seriesInfosFormPath = fileService.getSeriesInfosFormPath(mediaLibrary.getDirectory());
//            doubanInfo = douBanRequestService.getDoubanSeriesInfos(seriesInfosFormPath, searchApikey, customApikey);
//        }
//        if(doubanInfo == null || doubanInfo.getCode() != BaseResult.STATUS_SUCCESS){
//            assert doubanInfo != null;
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, doubanInfo.getMessage(), Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//        Object data = doubanInfo.getData();
//        if (data != null){
//            String dataStr = JSON.toJSONString(data);
//            JSONObject dataToJson = JSONObject.parseObject(dataStr);
//            infos = JSONObject.parseArray(dataToJson.getString("infos"), DoubanVideoInfo.class);
//            stars = JSONObject.parseArray(dataToJson.getString("stars"), Star.class);
//        }
//        // 存入数据库
//        dataToDataBase(infos, stars);
//        // 绑定 影视和豆瓣
//        linkVideoWithDouban(infos, type, mediaLibrary.getId());
//
//        // 修改标识
//        updateDoubanScheduleState(mediaLibrary.getId(), SuccessEnum.SUCCESS.getId());
//
//        // 添加观察者
//        addObserver(mediaLibrary);
//    }
//
//    /**
//     * 添加观察者
//     * @param mediaLibrary 媒体库信息实体
//     */
//    private void addObserver(MediaLibrary mediaLibrary) {
//        monitor.addObserver(FileListenerMonitorUtils.getFileAlterationObserver(mediaLibrary));
//    }
//
//    /**
//     * 绑定 影视和豆瓣
//     * @param infos 豆瓣信息
//     * @param type 视频类型： 0|电影，1|剧集
//     * @param mediaId 媒体库id
//     */
//    private void linkVideoWithDouban(List<DoubanVideoInfo> infos, Integer type, Long mediaId) {
//        List<MediaVideoDouban> mediaVideoDoubans = new ArrayList<>();
//        for (DoubanVideoInfo info : infos) {
//            String videoName = info.getVideoName();
//            if (type.equals(SubTypeEnum.MOVIE.getId())){
//                Movie one = movieService.getOne(new QueryWrapper<Movie>().lambda().eq(Movie::getMovieName, videoName));
//                if (one != null){
//                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
//                    mediaVideoDouban.setMediaId(mediaId);
//                    mediaVideoDouban.setItemId(one.getMovieId());
//                    mediaVideoDouban.setDoubanId(info.getDoubanInfo().getDoubanId());
//                    mediaVideoDoubans.add(mediaVideoDouban);
//                }
//            }
//            if (type.equals(SubTypeEnum.TV.getId())){
//                com.cosmax.media.system.commons.domain.douban.SeriesInfo seriesInfo = seriesInfoService.getOne(new QueryWrapper<com.cosmax.media.system.commons.domain.douban.SeriesInfo>().lambda().eq(com.cosmax.media.system.commons.domain.douban.SeriesInfo::getSeriesName, videoName));
//                if (seriesInfo != null){
//                    MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
//                    mediaVideoDouban.setMediaId(mediaId);
//                    mediaVideoDouban.setItemId(seriesInfo.getSeriesId());
//                    mediaVideoDouban.setDoubanId(info.getDoubanInfo().getDoubanId());
//                    mediaVideoDoubans.add(mediaVideoDouban);
//                }
//            }
//        }
//        // 删除mediaVideoDouban表中媒体库信息重新绑
//        mediaVideoDoubanService.removeById(mediaId);
//        boolean flag = mediaVideoDoubanService.saveBatch(mediaVideoDoubans);
//        if (!flag){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "绑定媒体库豆瓣视频失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "绑定媒体库豆瓣视频成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//    }
//
//    /**
//     * 删除原有数据
//     * @param mediaId 媒体库id
//     * @param type 媒体库类型，0|电影， 1|电视剧
//     */
//    private void initDatabase(Long mediaId, Integer type){
//        if (type.equals(SubTypeEnum.MOVIE.getId())){
//            // 删除电影明细
//            movieService.deleteByMediaId(mediaId);
//        }
//        if (type.equals(SubTypeEnum.TV.getId())){
//            // 删除剧集明细
//            seriesInfoService.deleteByMediaId(mediaId);
//        }
//    }
//
//    /**
//     * 存入数据库
//     * @param infos 豆瓣视频信息
//     * @param stars 演员信息
//     */
//    private void dataToDataBase(List<DoubanVideoInfo> infos, List<Star> stars) {
//        List<DoubanInfo> doubanInfos = new ArrayList<>();
//        List<DoubanDirector> doubanDirectors = new ArrayList<>();
//        List<DoubanWriter> doubanWriters = new ArrayList<>();
//        List<DoubanStar> doubanStars = new ArrayList<>();
//        List<Long> doubanIds = new ArrayList<>();
//
//        for (DoubanVideoInfo info : infos) {
//            DoubanInfo doubanInfo = info.getDoubanInfo();
//            doubanInfos.add(doubanInfo);
//            doubanIds.add(doubanInfo.getDoubanId()); // 用于删除豆瓣导演表等的id
//
//            // 获取导演
//            List<Long> directorList = info.getDirectorList();
//            for (Long directorId : directorList) {
//                DoubanDirector doubanDirector = new DoubanDirector();
//                doubanDirector.setDoubanId(doubanInfo.getDoubanId());
//                doubanDirector.setDirectorId(directorId);
////                doubanDirectorService.saveOrUpdate(doubanDirector);
//                doubanDirectors.add(doubanDirector);
//            }
//
//            // 获取编剧
//            List<Long> writerList = info.getWriterList();
//            for (Long writerId : writerList) {
//                DoubanWriter doubanWriter = new DoubanWriter();
//                doubanWriter.setDoubanId(doubanInfo.getDoubanId());
//                doubanWriter.setWriterId(writerId);
//                doubanWriters.add(doubanWriter);
//            }
//
//            // 获取演员
//            List<Long> actorList = info.getActorList();
//            for (Long actorId : actorList) {
//                DoubanStar doubanStar = new DoubanStar();
//                doubanStar.setDoubanId(doubanInfo.getDoubanId());
//                doubanStar.setStarId(actorId);
//                doubanStars.add(doubanStar);
//            }
//        }
//
//        // 写入数据库
//
//        // 豆瓣信息
//        boolean doubanInfoSave = doubanInfoService.saveOrUpdateBatch(doubanInfos);
//        if (!doubanInfoSave){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣信息保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣信息保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//
//        // 豆瓣导演
//        doubanDirectorService.removeByIds(doubanIds);
//        boolean doubanDirectorSave = doubanDirectorService.saveBatch(doubanDirectors);
//        if (!doubanDirectorSave){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣导演保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣导演保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//
//        // 豆瓣编剧
//        doubanWriterService.removeByIds(doubanIds);
//        boolean doubanWritersSave = doubanWriterService.saveBatch(doubanWriters);
//        if (!doubanWritersSave){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣编剧保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣编剧保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//
//        // 豆瓣演员
//        doubanStarService.removeByIds(doubanIds);
//        boolean doubanStarsSave = doubanStarService.saveOrUpdateBatch(doubanStars);
//        if (!doubanStarsSave){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣演员保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣演员保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//
//        boolean starsSave = starService.saveOrUpdateBatch(stars);
//        if (!starsSave){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "豆瓣演员信息保存失败！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }else {
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "豆瓣演员信息保存成功！", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//    }
//    /**
//     * 更新任务表
//     * @param libraryId 媒体库id
//     * @param state 状态：0|未完成，1|完成，2|异常
//     */
//    private void updateDoubanScheduleState(Long libraryId, Integer state){
//        MediaDoubanSchedule mediaDoubanSchedule = new MediaDoubanSchedule();
//        mediaDoubanSchedule.setMediaId(libraryId);
//        mediaDoubanSchedule.setState(state);
//        boolean success = mediaDoubanScheduleService.updateById(mediaDoubanSchedule);
//        if (!success){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "媒体库["+ libraryId +"]豆瓣任务更新失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//        LoggerUtils.sendMessage(getClass(), LoggerUtils.INFO_LOGGER, "媒体库["+ libraryId +"]豆瓣任务更新成功!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//    }
//
//    /**
//     * 更新任务表
//     * @param libraryId 媒体库id
//     * @param state 状态：0|创建，更新，1|删除
//     * @param isSuccess 成功标记：0为未完成，1为完成，2为异常
//     */
//    private void updateScheduleIsSuccess(Long libraryId, Integer state, Integer isSuccess){
//        com.cosmax.media.system.commons.domain.douban.MediaLibrarySchedule schedule = new com.cosmax.media.system.commons.domain.douban.MediaLibrarySchedule();
//        schedule.setLibraryId(libraryId);
//        schedule.setState(state);
//        schedule.setIsSuccess(isSuccess);
//
//        boolean update = scheduleService.updateById(schedule);
//        if (!update){
//            LoggerUtils.sendMessage(getClass(), LoggerUtils.WARN_LOGGER, "媒体库["+ libraryId +"]任务更新失败!", Thread.currentThread().getStackTrace()[1].getMethodName(), 1);
//        }
//    }
//
//    /**
//     * 通过季度和集数拼接字符串为S01E02样式
//     * @param season 季度
//     * @param episode 集
//     * @return 返回拼接字符串
//     */
//    private String getSeasonEpisode(Integer season, Integer episode){
//
//        DecimalFormat df = new DecimalFormat("00");
//        String seasonStr = df.format(season);
//        String episodeStr = df.format(episode);
//        return "S" + seasonStr + "E" + episodeStr;
//    }
//
//
//}
