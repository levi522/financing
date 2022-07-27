package com.r3.financing.util;


import java.util.HashMap;

/**
 * 用于返回Dto的工具类
 * Created by XX on 17-5-8.
 */
public class DtoUtil {

    public static String success = "true";

    public static String fail = "false";

    public static String errorCode = "0";

    /***
     * 统一返回成功的DTO
     */
    public static Dto returnSuccess() {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg("");
        dto.setErrorCode(errorCode);
        dto.setData(new HashMap<String,Object>());
        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static Dto returnCodeSuccess(String code, Object data) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg("");
        dto.setErrorCode(code);
        dto.setData(data);
        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static Dto returnSuccess(String message, Object data) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg(message);
        dto.setErrorCode(errorCode);
        dto.setData(data);
        return dto;
    }

    /***
     * 统一返回成功的DTO 不带数据
     */
    public static Dto returnSuccess(String message) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg(message);
        dto.setErrorCode(errorCode);
        dto.setData(new HashMap<String,Object>());
        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据 没有消息
     */
    public static Dto returnDataSuccess(Object data,String message) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setErrorCode(errorCode);
        dto.setData(data);
        dto.setMsg(message);
        return dto;
    }

    public static Dto returnFail(String message, String errorCode) {
        Dto dto = new Dto();
        dto.setSuccess(fail);
        dto.setMsg(message);
        dto.setData(new HashMap<String,Object>());
        dto.setErrorCode(errorCode);
        return dto;
    }

    public static Dto returnFail(String message, String errorCode,Object failData) {
        Dto dto = new Dto();
        dto.setSuccess(fail);
        dto.setMsg(message);
        dto.setData(failData);
        dto.setErrorCode(errorCode);
        return dto;
    }
}
