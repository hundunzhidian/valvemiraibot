package cn.qaq.valveqqrobot;

import cn.qaq.valveqqrobot.config.GlobalConfig;
import cn.qaq.valveqqrobot.service.QQService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    private QQService service;

    @Autowired
    private ObjectMapper objectMapper;
    @Async("qaqTaskExecutor")
    public void  run(){

        BotConfiguration configuration = new BotConfiguration();
        configuration.fileBasedDeviceInfo("deviceInfo.json");
        configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
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
        bot= BotFactory.INSTANCE.newBot(Long.parseLong(globalConfig.getQqnumber()),globalConfig.getQqpasswd(),configuration);
        log.info("MiraiBot加载成功，正在启动......");
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, event->{

            String msg=null;
            try {
                msg=service.msgHandle(String.valueOf(event.getSender().getId()),String.valueOf(event.getGroup().getId()),event.getMessage().contentToString());
            }catch (Exception e)
            {
                msg="【ERROR】处理消息出现异常:"+e.getMessage();

                log.error(msg);
            }finally {
                if(!StringUtils.isEmpty(msg))event.getGroup().sendMessage(new PlainText(msg));
            }
        });
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, event->{

            String msg=new String();
            try {
                msg=service.msgHandle(String.valueOf(event.getSender().getId()),null,event.getMessage().contentToString());
            }catch (Exception e)
            {
                msg="【ERROR】处理消息出现异常:"+e.getMessage();
                log.error(msg);
            }finally {
                if(!StringUtils.isEmpty(msg))event.getSender().sendMessage(new PlainText(msg));
            }
        });
        bot.login();
        log.info("MiraiBot启动成功");
        bot.join();
    }
}
