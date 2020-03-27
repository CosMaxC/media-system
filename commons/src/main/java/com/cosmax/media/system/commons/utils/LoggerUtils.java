package com.cosmax.media.system.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: media-system
 * @description: 封装logger
 * @author: Cosmax
 * @create: 2020/03/02 21:23
 */
public class LoggerUtils {

    public static final Integer INFO_LOGGER = 0;
    public static final Integer DEBUG_LOGGER = 1;
    public static final Integer WARN_LOGGER = 2;
    public static final Integer ERROR_LOGGER = 3;


    /**
     * 获取logger
     * @param clzz 所传的类 {@link Class}
     * @return {@link Logger}
     */
    private static Logger getLogger(Class clzz){

        return LoggerFactory.getLogger(clzz);
    }

    /**
     * 日志显示
     * @param clzz 发送日志的类
     * @param type 日志类型，0|info，1|debug，2|警告，3|错误
     * @param message 消息内容
     * @param methodName 方法名
     * @param i 方法内定位
     */
    public static void sendMessage(Class clzz, Integer type, String message, String methodName, int i){
        Logger logger = getLogger(clzz);

        message = methodName + "[" + i + "]" + ":" + message;
        if (type.equals(INFO_LOGGER)){
            logger.info(message);
        }

        if (type.equals(DEBUG_LOGGER)){
            logger.debug(message);
        }

        if (type.equals(WARN_LOGGER)){
            logger.warn(message);
        }
        if (type.equals(ERROR_LOGGER)){
            logger.error(message);
        }
    }

}
