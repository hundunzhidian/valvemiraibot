package cn.qaq.valveqqrobot.event;

import cn.qaq.valveqqrobot.service.QQService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @program: valveqqrobot
 * @description:
 * @author: QAQ
 * @create: 2020-08-19 10:34
 **/
@Slf4j
@Component
public class GroupListener extends SimpleListenerHost {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QQService service;
    @EventHandler
    public void onMessage(GroupMessageEvent event) {


        String msg=new String();
        try {
            msg=service.msgHandle(String.valueOf(event.getSender().getId()),String.valueOf(event.getGroup().getId()),event.getMessage().contentToString());
        }catch (Exception e)
        {
            msg="【ERROR】处理消息出现异常:"+e.getMessage();

            e.printStackTrace();
        }finally {
            if(msg!=null &&!msg.equals(""))event.getGroup().sendMessage(new PlainText(msg));
        }

    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        log.error("消息处理错误:"+exception.getMessage());
    }
}
