package com.zhou.aop.entity.http;

import lombok.Data;

import javax.validation.Valid;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/27 19:25
 * @Description: Http请求大对象
 */
@Data
public class HttpRequest<T>{
    /**
     * 客户端的版本
     */
    private String version;
    /**
     * 客户端的渠道
     */
    private String channel;
    /**
     * 客户端发起请求的时间
     */
    private long time;
    /**
     * 请求的数据对象
     */
    @Valid
    private T data;
    /**
     * 请求的密文，如果该接口需要加密上送，
     * 则将sdt的密文反序化到data，
     * sdt和action至少有一个为空
     */
    private String sdt;

}