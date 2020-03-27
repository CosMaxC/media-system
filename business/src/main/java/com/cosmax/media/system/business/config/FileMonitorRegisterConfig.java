package com.cosmax.media.system.business.config;

import com.cosmax.media.system.business.utils.FileListenerMonitorUtils;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.db.api.MediaLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class FileMonitorRegisterConfig {


   @Resource
   private FileListenerMonitorUtils fileListenerMonitorUtils ;

   @Autowired
   private MediaLibraryService mediaLibraryService;

   private FileAlterationMonitor monitor;

   @Bean
   public FileAlterationMonitor getFileAlterationMonitor(){
      return monitor;
   }

   @PostConstruct
   private void register() {
      // 监听对象为豆瓣搜索已完成，且媒体库搭建完成
      List<MediaLibrary> mediaLibraries = mediaLibraryService.getFinishedLibrary();

      Long intervalSeconds = 5L;
      String [] suffixs = {".mp4", ".mkv", ".avi", ".rmvb", ".rm", ".3gp", ".flv"};
      try {
         // 为指定文件夹下面的指定文件注册文件观察者
         monitor = fileListenerMonitorUtils.getMonitorByMediaLibrary(mediaLibraries, intervalSeconds, suffixs);
         // 启动观察者
         log.info("启动观察者！");
         monitor.start();
      } catch (Exception e) {
         log.error(e.getMessage());
      }

   }
}
