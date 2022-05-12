package com.zhou.scheduletask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/12 16:31
 * @Description:  定时任务
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScheduleTask {

    /**
     *  在线cron表达式生成器 https://cron.qqe2.com/
     */

    /**
     * 按照标准时间来算，每隔 10s 执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void job1() {
        log.info("【job1】开始执行：{}", new Date());
    }

    /**
     * 从启动时间开始，间隔 2s 执行
     * 固定间隔时间
     */
    @Scheduled(fixedRate = 2000)
    public void job2() {
        log.info("【job2】开始执行：{}",new Date());
    }

    /**
     * 从启动时间开始，延迟 5s 后间隔 4s 执行
     * 固定等待时间
     */
    @Scheduled(fixedDelay = 4000, initialDelay = 5000)
    public void job3() {
        log.info("【job3】开始执行：{}", new Date());
    }

    /**
     * 从配置文件读取执行间隔
     *
     */
    @Scheduled(cron = "${schedule.jobConfig}")
    public void job4() {
        log.info("【job4】开始执行：{}", new Date());
    }
}
