package com.lvchenglong.common;

import lombok.Data;

@Data
public class Result {
    private String code;
    private String msg;
    private Object data;

    private static final String CODE_SUCCESS = "200";
    private static final String MSG_SUCCESS = "请求成功";
    private static final String CODE_ERROR = "500";
    private static final String MSG_ERROR = "请求失败";

    public static Result success(){
        Result result = new Result();
        result.setCode(CODE_SUCCESS);
        result.setMsg(MSG_SUCCESS);
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setCode(CODE_ERROR);
        result.setMsg(MSG_ERROR);
        return result;
    }

    public static Result success(Object data){
        Result result = new Result();
        result.setCode(CODE_SUCCESS);
        result.setMsg(MSG_SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result success(String code,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.setCode(CODE_ERROR);
        result.setMsg(msg);
        return result;
    }

    public static Result error(String code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
