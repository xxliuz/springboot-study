package com.zhou.aop.service;

import com.zhou.aop.entity.request.TestRequest;
import com.zhou.aop.entity.response.TestResponse;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/27 20:03
 * @Description:
 */
public interface TestService {

    TestResponse get(TestRequest testRequest) throws Exception;

}