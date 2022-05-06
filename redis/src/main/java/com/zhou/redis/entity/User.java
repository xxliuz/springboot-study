package com.zhou.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/6 15:05
 * @Description: 用户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 2892248514883451461L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
}
