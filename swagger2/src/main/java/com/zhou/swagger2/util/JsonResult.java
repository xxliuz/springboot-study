package com.zhou.swagger2.util;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/25 16:54
 * @Description:
 */
public class JsonResult {
    private String code;
    private String message;
    private Object data;

    public JsonResult() {
        this.setCode(ResultCode.SUCCESS);
        this.setMessage(ResultCode.SUCCESS.msg());
    }

    public JsonResult(ResultCode code) {
        this.setCode(code);
        this.setMessage(code.msg());
    }

    public JsonResult(ResultCode code, String data) {
        this.setCode(code);
        this.setMessage(code.msg());
        this.setData(data);
    }

    public JsonResult(ResultCode code, Object data) {
        this.setCode(code);
        this.setMessage(code.msg());
        this.setData(data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code.val();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJsonString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}