package com.cosmax.media.system.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @program: media-system
 * @description: 通用文件工具类
 * @author: Cosmax
 * @create: 2020/02/06 23:15
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 判断是否系统为linux
     * @return boolean true为是，false为否
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 判断是否系统为linux
     * @return boolean true为是，false为否
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 下载文件到本地
     * @param url 文件链接
     * @param dir 下载到本地目录
     * @param fileName 文件名
     * @param suffix 后缀
     * @return String dir/fileName.suffix
     */
    public static String downloadFromUrl(String url, String dir, String fileName, String suffix){
        String path;
        File dirFile = new File(dir);
        if (!dirFile.exists()){
            boolean mkdirs = dirFile.mkdirs();
            if (!mkdirs){
                return null;
            }
        }
        // 拼接字符串 判断系统
        if (isWindows() || isLinux()){
            path = dir + "/" + fileName + "." + suffix;
        }else {
            return null;
        }
        try {
            copyURLToFile(new URL(url), new File(path),5000, 5000);
            return path;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 下载图片到指定目录
     * @param url 文件链接
     * @param dir 下载到本地目录
     * @param fileName 文件名
     * @param urlPath 最终返回的文件路径+后缀（网络请求地址）
     * @return String dir/fileName.suffix
     */
    public static String downloadJPEGFromUrl(String url, String dir, String fileName, String urlPath){
        String suffix;
        if (url.toLowerCase().contains("webp")){
            suffix = "jpeg";
        }else {
            // 获取图片文件后缀
            String[] split = url.split("\\.");
            suffix = split[split.length - 1].toLowerCase();
        }
        String res = downloadFromUrl(url, dir, fileName, suffix);
        if (StringUtils.isBlank(res)){
            return null;
        }else {
            return urlPath + "." + suffix;
        }
    }
    /**
     * 获取默认下载目录
     * @return String 下载目录
     */
    private static String getDefaultDownloadPath(){
        if (isWindows()){
            // 获取windows下目录
            // 将图片库放在非系统盘的最大剩余空间磁盘下
            long maxFreeSize = 0;   // 获取最大剩余空间
            String maxFreePath = null;
            String property = System.getProperty("user.home"); // 用户文件夹目录位置(判断系统磁盘位置)
            File [] files = File.listRoots();
            for (File file : files) {
                String path = file.getPath();
                if (property.contains(path)){
                    continue;
                }
                long freeSpace = file.getFreeSpace();
                if (freeSpace > maxFreeSize){
                    maxFreeSize = freeSpace;
                    maxFreePath = path;
                }
            }
            return maxFreePath == null ? null : maxFreePath + "media-system\\photos";
        }
        if (isLinux()){
            // 获取linux下目录
            return "/usr/local/media-system/photos";
        }
        return null;
    }

    /**
     * 创建硬链接
     * @param linkPath 需要链接到的文件路径（目标地址）
     * @param sourcePath 源文件路径
     * @return 成功返回目标路径，失败为空
     */
    public static String createLink(String linkPath, String sourcePath){

        Path link = Paths.get(linkPath);
        Path source = Paths.get(sourcePath);

        // 判断父目录是否存在，不存在需要创建
        Path linkParent = link.getParent();
        if (!Files.exists(linkParent)){
            try {
                Files.createDirectories(linkParent);
            } catch (IOException e) {
                logger.warn("父目录无法创建！", e);
                return null;
            }
        }
        if (Files.exists(link)){
            logger.info("硬链接已存在！无需更新");
            return null;
        }

        try {
            Files.createLink(link, source);
            return linkPath;
        } catch (IOException e) {
            logger.warn("硬链接创建失败！", e);
            return null;
        }
    }
    /**
     * 获取硬链接，返回映射地址
     * @param sourcePath 源文件路径
     * @param mediaLibraryId 媒体库id
     * @param fileName 硬链接后文件名含后缀
     * @return 成功返回目标路径，失败为空
     */
    public static String createLink(String sourcePath, Long mediaLibraryId, String fileName){
        String linkPath = getJarFilePath() + "/videos/" + mediaLibraryId + "/" + fileName;
        if (StringUtils.isBlank(createLink(linkPath, sourcePath))){
            return null;
        }
        return "/videos/" + mediaLibraryId + "/" + fileName;
    }

    /**
     * 获取springboot运行的jar目录
     * @return String jar运行目录
     */
    public static String getJarFilePath() {
        ApplicationHome home = new ApplicationHome(FileUtils.class);
        File jarFile = home.getSource();
        return jarFile.getParentFile().getParent() + "/data";
//        return "D:/media/media-system";
    }

}
