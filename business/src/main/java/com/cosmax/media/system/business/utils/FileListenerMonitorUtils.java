package com.cosmax.media.system.business.utils;

import com.cosmax.media.system.business.listener.VideoFileListener;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class FileListenerMonitorUtils {

   @Resource
   private VideoFileListener videoFileListener;

   /**
    * 生成monitor,监听多个媒体库文件夹下面的以suffix结束的文件
    * @param mediaLibraries 已完成的媒体库list
    * @param intervalSeconds 轮询时间
    * @param suffixs 后缀数组
    * @return 文件观察者 {@link FileAlterationMonitor}
    */
   public FileAlterationMonitor getMonitorByMediaLibrary(List<MediaLibrary> mediaLibraries, Long intervalSeconds, String[] suffixs){
       // 设置扫描间隔
       long interval = TimeUnit.SECONDS.toMillis(intervalSeconds);

       // 创建过滤器
       // 匹配目录
       IOFileFilter directoryIOFileFilter = FileFilterUtils.and(FileFilterUtils.directoryFileFilter());
       // 匹配视频后缀文件
       IOFileFilter [] ioFileFilters = new IOFileFilter[suffixs.length + 1];// 1是文件夹
       for (int i = 0; i < suffixs.length; i++) {
           ioFileFilters[i] = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(suffixs[i]));
       }
       ioFileFilters[suffixs.length] = directoryIOFileFilter;
       // 过滤符合：对应可视目录 和 后缀为视频格式的文件
       IOFileFilter fileFilter = FileFilterUtils.or(ioFileFilters);

       // 使用过滤器：装配过滤器，生成监听者
       FileAlterationObserver []  fileAlterationObservers = new FileAlterationObserver[mediaLibraries.size()];
       for (int i = 0; i < mediaLibraries.size(); i++) {
           File file = new File(mediaLibraries.get(i).getDirectory());
           if (!file.exists()){
               log.info("文件夹["+ mediaLibraries.get(i) +"]不存在！");
               continue;
           }
           FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(file, fileFilter);
           // 向监听者添加监听器，并注入业务服务
           VideoFileListener videoFileListener = new VideoFileListener();
           videoFileListener.setMediaLibrary(mediaLibraries.get(i));
           fileAlterationObserver.addListener(videoFileListener);
           fileAlterationObservers[i] = fileAlterationObserver;
       }
        return new FileAlterationMonitor(interval, fileAlterationObservers);
   }

    /**
     * 通过媒体库实体封装观察者实体
     * @param mediaLibrary 媒体库实体 {@link MediaLibrary}
     * @return 观察者实体 {@link FileAlterationObserver}
     */
    public static FileAlterationObserver getFileAlterationObserver(MediaLibrary mediaLibrary) {
        File file = new File(mediaLibrary.getDirectory());
        if (!file.exists()) {
            log.warn("文件夹[" + mediaLibrary.getDirectory() + "]不存在！");
            return null;
        }
        String [] suffixs = {".mp4", ".mkv", ".avi", ".rmvb", ".rm", ".3gp", ".flv"};
        // 创建过滤器
        // 匹配目录
        IOFileFilter directoryIOFileFilter = FileFilterUtils.and(FileFilterUtils.directoryFileFilter());
        // 匹配视频后缀文件
        IOFileFilter [] ioFileFilters = new IOFileFilter[suffixs.length + 1];// 1是文件夹
        for (int i = 0; i < suffixs.length; i++) {
            ioFileFilters[i] = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(suffixs[i]));
        }
        ioFileFilters[suffixs.length] = directoryIOFileFilter;
        // 过滤符合：对应可视目录 和 后缀为视频格式的文件
        IOFileFilter fileFilter = FileFilterUtils.or(ioFileFilters);
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(file, fileFilter);
        // 向监听者添加监听器，并注入业务服务
        VideoFileListener videoFileListener = new VideoFileListener();
        videoFileListener.setMediaLibrary(mediaLibrary);
        fileAlterationObserver.addListener(videoFileListener);
        return fileAlterationObserver;
    }
}
