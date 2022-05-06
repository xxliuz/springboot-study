package com.zhou.redis.service;

import com.zhou.redis.entity.User;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/6 15:05
 * @Description:
 */
public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    User saveOrUpdate(User user);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);
}