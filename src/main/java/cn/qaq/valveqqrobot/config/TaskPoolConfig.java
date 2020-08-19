package cn.qaq.valveqqrobot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: QaQCloud
 * @description: 线程池配置
 * @author: QAQ
 * @create: 2019-12-17 13:42
 **/
@Configuration
@EnableAsync
@Slf4j
public class TaskPoolConfig {
    @Bean("qaqTaskExecutor")
    public Executor qaqTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);//核心线程数
        executor.setMaxPoolSize(20); //最大线程数
        executor.setQueueCapacity(200);//队列中最大的数
        executor.setKeepAliveSeconds(60);        //线程空闲后最大的存活时间
        executor.setThreadNamePrefix("QaQtaskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        log.info("线程池初始化成功......");
        return executor;
    }
}
