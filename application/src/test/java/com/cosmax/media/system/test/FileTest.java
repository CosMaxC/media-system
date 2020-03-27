package com.cosmax.media.system.test;

import com.alibaba.fastjson.JSON;
import com.cosmax.media.system.MediaSystemApplication;
import com.cosmax.media.system.commons.domain.SeriesInfo;
import com.cosmax.media.system.commons.utils.FileUtils;
import com.cosmax.media.system.commons.utils.MediaFileUtils;
import com.cosmax.media.system.commons.validation.ValuesValidation;
import com.cosmax.media.system.file.api.FileService;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cosmax.media.system.commons.utils.FileUtils.getJarFilePath;

/**
 * @program: media-system
 * @description:
 * @author: Cosmax
 * @create: 2020/02/02 17:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediaSystemApplication.class)
public class FileTest {

    @Autowired
    private FileService fileService;

    @Test
    public void getList(){
//        System.out.println(JSON.toJSONString(fileService.getMovieInfosFromPath("E:\\movies")));;
        System.out.println(FileUtils.getJarFilePath());
    }

    @Test
    public void getName(){
        String str = "Youth.2015.BluRay.1080p.DTS-HD.MA5.1.x265.10bit-BeiTai.mkv";
        Pattern pattern = Pattern.compile("^[\\w\\W]*(19[0-9]{2}|20[0-9]{2})");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void replaceTest(){
        String str = "Ford.v.Ferrari.2019.BluRay.1080p.DTS-HD.MA.7.1.x265.10bit-BeiTai.mkv";
        Pattern pattern = Pattern.compile("^[\\w\\W]*(19[0-9]{2}|20[0-9]{2})");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String group = matcher.group(0);
            String replace = group.replaceAll("(19[0-9]{2}|20[0-9]{2})", "");
            replace = replace.replaceAll("[\\W]"," ");
            System.out.println(replace);
            System.out.println("year:" + matcher.group(1));
        }
    }

    @Test
    public void seriesTest(){
//        String path = "E:\\series";
//        System.out.println(JSON.toJSONString(fileService.getSeriesInfosFormPath(path)));
//        File file = new File(path);
//        File[] files = file.listFiles();
//        for (File file1 : files) {
//            if (file1.isDirectory()){
//                System.out.println("---------------------------");
//                String absoluteFile = file1.getAbsoluteFile().toString();
//                System.out.println("路径：" + file1.getAbsoluteFile());
//                System.out.println("目录名：" + FilenameUtils.getBaseName(absoluteFile));
//                String seriesName = FilenameUtils.getBaseName(absoluteFile);
//                Pattern pattern = Pattern.compile("^[\\w\\W]*[S|s]\\d{1,3}");
//                Matcher matcher = pattern.matcher(seriesName);
//                if (matcher.find()){
//                    String group = matcher.group(0);
//                    System.out.println("group:" + group);
//                    Matcher matcher1 = Pattern.compile("[S|s]\\d{1,3}").matcher(group);
//                    if (matcher1.find()){
//
//                        System.out.println("季度：" + matcher1.group(0));
//                    }
//                    String s = group
//                            .replaceAll(".[S|s]\\d{1,3}", "")
//                            .replaceAll("\\W"," ");;
//
//                    System.out.println("名字：" + s);
//
//                }
//                System.out.println("---------------------------");
//
//            }
//        }
        File file = new File("D:/media/media-system/1583572872452/Agents of S H I E L D.S01E01.mkv");
        SeriesInfo seriesInfoByFile = MediaFileUtils.getSeriesInfoByFile(file);
        System.out.println(JSON.toJSONString(seriesInfoByFile));
        System.out.println(JSON.toJSONString(MediaFileUtils.getEpisodeInfoByFile(file)));
    }

    @Test
    public void linkTest(){
        Path targetFile = Paths.get("E:\\迅雷下载\\huishenghuiying2019pro_trial_64bit.zip");
//        Path linkFile = Paths.get("E:\\迅雷下载\\huishenghuiying2019pro_trial_64bit - 副本.zip");
        Path linkFile = Paths.get("E:/test/abc/1.zip");
        Path parent = linkFile.getParent();
        System.out.println(parent);
        if (!Files.exists(parent)) {
            System.out.println("不存在");
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(Files.exists(parent));
            return;
        }
        try {
            if (Files.exists(linkFile)){
                Files.delete(linkFile);
            }
            Path link = Files.createLink(linkFile, targetFile);
            System.out.println(link.getName(0));
            System.out.println(link.getName(link.getNameCount()-1));
            System.out.println(link.getFileName()); // 需要
            System.out.println(link.getFileSystem());
            System.out.println(link.getNameCount());
            System.out.println(link.getParent()); // 需要
            System.out.println(link.getRoot());
//            boolean sameFile = Files.isSameFile(targetFile, linkFile);
//            System.out.println(sameFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
