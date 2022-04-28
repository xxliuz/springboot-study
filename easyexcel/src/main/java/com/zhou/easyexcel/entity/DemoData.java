package com.zhou.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 15:27
 * @Description:
 */
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}