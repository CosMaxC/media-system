package com.cosmax.media.system.business.config;

import com.cosmax.media.system.business.business.MediaLibraryBusiness;
import com.cosmax.media.system.business.listener.VideoFileListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class FileInitRun implements CommandLineRunner {

    @Autowired
    private MediaLibraryBusiness mediaLibraryBusiness;

    @Override
    public void run(String... args) throws Exception {
        //解决listener注入不了spring容器对象的问题
        VideoFileListener.setMediaLibraryBusiness(mediaLibraryBusiness);
    }
}
