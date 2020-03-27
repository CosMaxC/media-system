package com.cosmax.media.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @program: media-system
 * @description: 启动文件
 * @author: Cosmax
 * @create: 2020/02/02 17:30
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.cosmax")
@MapperScan(basePackages = "com.cosmax.media.system.db.provider.mapper")
public class MediaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaSystemApplication.class, args);
    }
}
