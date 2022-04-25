package com.zhou.swagger2.controller;

import com.zhou.swagger2.util.JsonResult;
import com.zhou.swagger2.util.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/25 16:36
 * @Description:
 */
@Slf4j
@RestController
public class TestController {

    /**
     * http://localhost:9090/hello
     *
     * @return
     */
    @ApiOperation(value = "/hello 欢迎入口", httpMethod = "GET")
    @RequestMapping(value = "/hello")
    public String hello() {
        log.info("hello");
        return "Hello greetings from spring-boot2-swagger";
    }

    @ApiOperation(value = "/getUserName 根据用户id获得用户的姓名", notes = "id不能为空", httpMethod = "GET")
    @ApiImplicitParam(dataType = "string", name = "userId", value = "用户id", required = true)
    @RequestMapping(value = "/getUserName")
    public JsonResult getUserName(String userId) {
        String result = "hello " + userId + "，name=张三";
        return new JsonResult(ResultCode.SUCCESS, result);
    }

    /**
     * Swagger注解用法：
     *
     * @Api：修饰整个类，描述Controller的作用
     * @ApiOperation：描述一个类的一个方法，或者说一个接口
     * @ApiParam：单个参数描述
     * @ApiModel：用对象来接收参数
     * @ApiProperty：用对象接收参数时，描述对象的一个字段
     * @ApiResponse：HTTP响应其中1个描述
     * @ApiResponses：HTTP响应整体描述
     * @ApiIgnore：使用该注解忽略这个API
     * @ApiError ：发生错误返回的信息
     * @ApiImplicitParam：一个请求参数
     * @ApiImplicitParams：多个请求参数
     */
}
