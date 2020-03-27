package com.cosmax.media.system.business.listener;

import com.cosmax.media.system.business.business.MediaLibraryBusiness;
import com.cosmax.media.system.business.business.impl.MediaLibraryBusinessImpl;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import com.cosmax.media.system.commons.utils.MediaFileUtils;
import com.cosmax.media.system.db.api.MovieService;
import com.cosmax.media.system.db.api.SeriesInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

/**
 * @program: hello-observer
 * @description: 文件监听动作
 * @author: Cosmax
 * @create: 2020/03/08 10:43
 */

@Slf4j
@Component
public class VideoFileListener extends FileAlterationListenerAdaptor implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(VideoFileListener.class);

    public MediaLibrary getMediaLibrary() {
        return mediaLibrary;
    }

    private MediaLibrary mediaLibrary;

    private static MediaLibraryBusiness mediaLibraryBusiness;

    private static MovieService movieService;

    private static SeriesInfoService seriesInfoService;

    public static void setMovieService(MovieService movieService) {
        VideoFileListener.movieService = movieService;
    }

    public void setMediaLibrary(MediaLibrary mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }

    public static MediaLibraryBusiness getMediaLibraryBusiness() {
        return mediaLibraryBusiness;
    }


    public static void setMediaLibraryBusiness(MediaLibraryBusiness mediaLibraryBusiness) {
        VideoFileListener.mediaLibraryBusiness = mediaLibraryBusiness;
    }

    @Override
    public void onFileDelete(File file) {
        super.onFileDelete(file);
        String canonicalPath = getCanonicalPath(file);
        if (StringUtils.isBlank(canonicalPath)){
            logger.warn("获取绝对路径失败！");
            return;
        }
        BaseResult baseResult = mediaLibraryBusiness.deleteVideoToMediaLibrary(mediaLibrary, canonicalPath);
        if (baseResult.getCode() != BaseResult.STATUS_SUCCESS){
            log.warn(baseResult.getMessage());
            return;
        }
        log.info("["+ mediaLibrary.getId() +"]删除["+ canonicalPath +"]成功！");

    }

    @Override
    public void onFileCreate(File file) {
        super.onFileCreate(file);
        String canonicalPath = getCanonicalPath(file);
        if (StringUtils.isBlank(canonicalPath)){
            logger.warn("获取绝对路径失败！");
            return;
        }
        BaseResult baseResult = mediaLibraryBusiness.insertVideoToMediaLibrary(mediaLibrary, canonicalPath);
        if (baseResult.getCode() != BaseResult.STATUS_SUCCESS){
            log.warn(baseResult.getMessage());
            return;
        }
        log.info("["+ mediaLibrary.getId() +"]添加["+ canonicalPath +"]成功！");
    }

    /**
     * 获取标准的绝对路径
     * @param file 文件File
     * @return 绝对路径字符，空为获取失败
     */
    private String getCanonicalPath(File file){
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        mediaLibraryBusiness = WebApplicationContextUtils
                .getWebApplicationContext(sce.getServletContext())
                .getBean(MediaLibraryBusinessImpl.class);


    }
}
