package com.zhou.easyexcel.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 16:19
 * @Description: 异常数据
 */
@Getter
@Setter
@EqualsAndHashCode
public class ExceptionDemoData {
    /**
     * 用日期去接字符串 肯定报错
     */
    private Date date;
}

