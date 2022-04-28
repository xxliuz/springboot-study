package com.zhou.aop.controller;

import com.zhou.aop.annotation.HttpCheck;
import com.zhou.aop.entity.http.HttpRequest;
import com.zhou.aop.entity.http.HttpResponse;
import com.zhou.aop.entity.request.TestRequest;
import com.zhou.aop.entity.response.TestResponse;
import com.zhou.aop.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 9:29
 * @Description:
 */
@Api(tags = "test")
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    TestService testService;

    /**
     * 测试接口1
     * @param httpRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test1", produces = "application/json; charset=UTF-8")
    @HttpCheck(dataType = TestRequest.class,isDecrypt = false)
    @ApiOperation(value = "test1")
    public HttpResponse<TestResponse> Test1(@RequestBody HttpRequest<TestRequest> httpRequest) throws Exception {
        TestResponse testResponse = testService.get(httpRequest.getData());
        return HttpResponse.build(200, "成功！", testResponse);
    }

    /**
     * 测试接口2
     * @param httpRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test2", produces = "application/json; charset=UTF-8")
    @HttpCheck(dataType = TestRequest.class, isTimeout = false)
    @ApiOperation(value = "test2")
    public HttpResponse<TestResponse> Test2(@RequestBody HttpRequest<TestRequest> httpRequest) throws Exception {
        TestResponse testResponse = testService.get(httpRequest.getData());
        return HttpResponse.build(200, "成功！", testResponse);
    }

    /**
     * 测试接口3
     * @param httpRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test3", produces = "application/json; charset=UTF-8")
    @HttpCheck(dataType = TestRequest.class, isDecrypt = false, isTimeout = false)
    @ApiOperation(value = "test3")
    public HttpResponse<TestResponse> Test3(@RequestBody HttpRequest<TestRequest> httpRequest) throws Exception {
        TestResponse testResponse = testService.get(httpRequest.getData());
        return HttpResponse.build(200, "成功！", testResponse);
    }

    /**
     * 测试接口4
     * @param httpRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test4", produces = "application/json; charset=UTF-8")
    @HttpCheck(dataType = TestRequest.class, isEncrypt = false, isTimeout = false)
    @ApiOperation(value = "test4")
    public HttpResponse<TestResponse> Test4(@RequestBody HttpRequest<TestRequest> httpRequest) throws Exception {
        TestResponse testResponse = testService.get(httpRequest.getData());
        return HttpResponse.build(200, "成功！", testResponse);
    }

    /**
     * 测试接口5
     * @param httpRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test5", produces = "application/json; charset=UTF-8")
    @HttpCheck(dataType = TestRequest.class, isDecrypt = false, isEncrypt = false, isTimeout = false)
    @ApiOperation(value = "test5")
    public HttpResponse<TestResponse> Test5(@RequestBody HttpRequest<TestRequest> httpRequest) throws Exception {
        TestResponse testResponse = testService.get(httpRequest.getData());
        return HttpResponse.build(200, "成功！", testResponse);
    }

}