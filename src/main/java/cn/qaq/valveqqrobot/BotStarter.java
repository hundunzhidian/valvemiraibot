package cn.qaq.valveqqrobot;

import cn.qaq.valveqqrobot.config.GlobalConfig;
import cn.qaq.valveqqrobot.event.FriendListener;
import cn.qaq.valveqqrobot.event.GroupListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @program: valveqqrobot
 * @description:
 * @author: QAQ
 * @create: 2020-08-18 10:52
 **/
@Component
@Slf4j
public class BotStarter {
    public static Bot bot = null;

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private FriendListener friendListener;
    @Autowired
    private GroupListener groupListener;

    @Autowired
    private ObjectMapper objectMapper;
    @Async("qaqTaskExecutor")
    public void  run(){

    BotConfiguration configuration=new BotConfiguration();
    configuration.fileBasedDeviceInfo();
    switch (globalConfig.getLogflag().toLowerCase()){
        case "all":
            log.info("已启动所有日志");
            break;
        case "net":
            log.info("已启动net日志");
            configuration.noBotLog();
            break;
        case "bot":
            log.info("已启动bot日志");
            configuration.noNetworkLog();
            break;
        default :
            log.info("已禁用所有日志信息");
            configuration.noNetworkLog();
            configuration.noBotLog();
    }
    bot=BotFactoryJvm.newBot(Long.parseLong(globalConfig.getQqnumber()),globalConfig.getQqpasswd(),configuration);
        log.info("MiraiBot加载成功，正在启动......");

        bot.login();
    log.info("MiraiBot启动成功");
        Events.registerEvents(bot,friendListener);
        Events.registerEvents(bot,groupListener);
    bot.join();
    }
}
