package com.lvchenglong.common.system;

import cn.hutool.core.date.DateUtil;
import com.lvchenglong.entity.Logs;
import com.lvchenglong.mapper.LogsMapper;
import com.lvchenglong.utils.IpUtils;
import com.lvchenglong.utils.SpringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 管理所有多线程任务的线程执行工厂类
 */
public class AsyncTaskFactory {

    private static final ThreadPoolTaskExecutor EXECUTOR = SpringUtils.getBean("threadPoolTaskExecutor");
    /**
     * 定义多线程执行任务
     * 记录日志
     * 模块、操作、操作人的ID、IP、操作时间
     */
    public static void recordLog(String module,String operate,Integer userId){
        String ip = IpUtils.getIpAddr();
        EXECUTOR.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 创建日志对象，存储到数据库
            Logs logs = Logs.builder()
                    .module(module)
                    .operate(operate)
                    .userId(userId)
                    .ip(ip)
                    .time(DateUtil.now())
                    .build();
            LogsMapper logsMapper = SpringUtils.getBean(LogsMapper.class);
            logsMapper.insert(logs);
            System.out.println("============日志线程执行结束============");
        });
    }
}
