package com.cosmax.media.system.commons;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: media-system
 * @description: 数据传递返回实体
 * @author: Cosmax
 * @create: 2020/02/01 17:36
 */
@Data
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 3468352004150968551L;

    public static final int STATUS_SUCCESS = 20000;
    public static final int STATUS_FAIL = 50000;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回对象
     */
    private Object data;


    public static BaseResult success(){
        return BaseResult.createResult(STATUS_SUCCESS,"成功",null);
    }

    public static BaseResult success(String message){
        return BaseResult.createResult(STATUS_SUCCESS, message,null);
    }

    public static BaseResult success(String message,Object data){
        return BaseResult.createResult(STATUS_SUCCESS, message,data);
    }

    public static BaseResult fail(){
        return createResult(STATUS_FAIL,"失败",null);
    }

    public static BaseResult fail(String message){
        return createResult(STATUS_FAIL,message,null);
    }

    public static BaseResult fail(String message,Object data){
        return createResult(STATUS_FAIL,message,data);
    }

    public static BaseResult fail(int status, String message,Object data){
        return createResult(status,message,data);
    }

    private static BaseResult createResult(int status, String message, Object data){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(status);
        baseResult.setMessage(message);
        baseResult.setData(data);
        return baseResult;
    }
}
