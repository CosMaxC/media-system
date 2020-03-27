package com.cosmax.media.system.commons.validation;

import org.apache.commons.io.FilenameUtils;

/**
 * @program: media-system
 * @description: 文件类型判断
 * @author: Cosmax
 * @create: 2020/02/03 13:50
 */
public class FileValidation {

    /**
     * 判断是否为视频文件
     * @param absolutePath 文件绝对路径
     * @return boolean
     */
    public static boolean checkVideo(String absolutePath){
        String lowExtension = getExtensionByAbsolutePath(absolutePath).toLowerCase();
        return lowExtension.contains("mp4")
                || lowExtension.contains("mkv")
                || lowExtension.contains("avi")
                || lowExtension.contains("rmvb")
                || lowExtension.contains("rm")
                || lowExtension.contains("3gp")
                || lowExtension.contains("flv");
    }

    /**
     * 判断是否为字幕文件
     * @param absolutePath 文件绝对路径
     * @return boolean
     */
    public static boolean checkSubtitle(String absolutePath){
        String lowExtension = getExtensionByAbsolutePath(absolutePath).toLowerCase();
        return lowExtension.contains("srt")
                || lowExtension.contains("ssa")
                || lowExtension.contains("ass");
    }

    /**
     * 判断是否为音频文件
     * @param absolutePath 文件绝对路径
     * @return boolean
     */
    public static boolean checkMusic(String absolutePath){
        String lowExtension = getExtensionByAbsolutePath(absolutePath).toLowerCase();
        return lowExtension.contains("wav")
                || lowExtension.contains("mp3")
                || lowExtension.contains("wma")
                || lowExtension.contains("flac")
                || lowExtension.contains("ape");
    }

    private static String getExtensionByAbsolutePath(String absolutePath){
        return FilenameUtils.getExtension(absolutePath);
    }
}
