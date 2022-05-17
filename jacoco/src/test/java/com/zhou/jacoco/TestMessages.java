package com.zhou.jacoco;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/17 15:23
 * @Description:
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JacocoApplication.class)
public class TestMessages {

    @Test
    public void testMessage()
    {
        Messages obj=new Messages();
        Assertions.assertEquals("hello saipriyadarshini bandi",obj.getMessage("hello saipriyadarshini bandi"));
    }
}