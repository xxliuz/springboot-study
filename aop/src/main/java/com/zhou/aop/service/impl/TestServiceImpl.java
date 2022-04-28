package com.zhou.aop.service.impl;

import com.zhou.aop.entity.request.TestRequest;
import com.zhou.aop.entity.response.TestResponse;
import com.zhou.aop.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/27 20:04
 * @Description:
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public TestResponse get(TestRequest testRequest) throws Exception {
        TestResponse testResponse = new TestResponse();
        testResponse.setId(testRequest.getId());
        testResponse.setName(testRequest.getName());
        testResponse.setSex(testRequest.getSex());
        return testResponse;
    }

}