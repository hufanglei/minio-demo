package com.jsu.vo;

import lombok.Data;

@Data
public class ApiResult {
    private int code;
    private String msg;
    private Object data;

    public static ApiResult success(Object data) {
        return success(200, "操作成功", data);
    }
    public static ApiResult success(String msg){
        return success(200,msg,null);
    }

    public static ApiResult success(int code, String msg, Object data) {
        ApiResult r = new ApiResult();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static ApiResult error(String msg) {
        return error(400, msg, null);
    }

    public static ApiResult error(int code, String msg, Object data) {
        ApiResult r = new ApiResult();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
