package cn.qaq.valveqqrobot.service;

import ch.qos.logback.core.joran.conditional.ElseAction;
import cn.qaq.valveqqrobot.config.GlobalConfig;
import cn.qaq.valveqqrobot.utils.TcpTools;
import cn.qaq.valveqqrobot.utils.UdpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: QaQCloud
 * @description: 服务
 * @author: QAQ
 * @create: 2019-09-03 10:17
 **/
@Service
@Slf4j
public class QQService {


    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private ObjectMapper objectMapper;

    public String getServerPlayers(String ip) {
        log.debug(ip);
        StringBuilder stringBuilder=new StringBuilder();
        try {
            List<HashMap<String,Object>> jsonArray= UdpServer.getPlayers(ip);
            log.debug(objectMapper.writeValueAsString(jsonArray));
            stringBuilder.append("当前玩家:");
            stringBuilder.append(jsonArray.size());
            stringBuilder.append("个\n");
            for(int i=0;i<jsonArray.size();i++)
            {
                stringBuilder.append(jsonArray.get(i).get("name"));
                stringBuilder.append("     ");
                Long time=Double.valueOf(String.valueOf(jsonArray.get(i).get("time"))).longValue();
                if(time>3600)
                {
                    stringBuilder.append(time/3600);
                    stringBuilder.append("h");
                    stringBuilder.append(time/60%60);
                    stringBuilder.append("m");
                    stringBuilder.append(time%60);
                    stringBuilder.append("s");
                }else if(time>60){
                    stringBuilder.append(time/60);
                    stringBuilder.append("m");
                    stringBuilder.append(time%60);
                    stringBuilder.append("s");
                }else {
                    stringBuilder.append(time);
                    stringBuilder.append("s");
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append("--------------------\n");

        }
        catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            stringBuilder.append("【访问异常】");
        }
        return stringBuilder.toString();
    }
    public String getServerInfo(String ip)
    {
        log.debug(ip);
        HashMap<String,Object> jsonObject1=UdpServer.getServers(ip);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("服务器名称: ");
        stringBuilder.append(jsonObject1.get("name"));
        stringBuilder.append("\n当前地图: ");
        stringBuilder.append(jsonObject1.get("map"));
        stringBuilder.append("\n当前人数/Max: ");
        stringBuilder.append(jsonObject1.get("players"));
        stringBuilder.append("\n延迟：");
        stringBuilder.append(jsonObject1.get("time"));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
    public boolean isIsmanager(String sender)
    {
        if(globalConfig.getManaQQs().contains(sender)) return true;
        return false;
    }
    public String msgHandle(String sender,String group,String content)throws Exception
    {
        log.debug(sender+" "+content);
        boolean ismaga=isIsmanager(sender);
        if(!globalConfig.isQQenable(group)) return null;
        LinkedHashMap<String,GlobalConfig.server> servers=globalConfig.getServerMap();
        //下面开始分析....
        if(content==null||!(content.contains("/"))) return null;
        if(content.startsWith("/players"))
        {
            if(!content.equals("/players"))
            {
                if(servers.containsKey(content
                        .replace("/players ","")
                        .replace(" ","")))
                {
                    return getServerPlayers(servers.get(content
                            .replace("/players ","")
                            .replace(" ","")).getIp());
                }
                    else return getServerPlayers(content
                    .replace("/players ","")
                    .replace(" ",""));
            }
            StringBuilder stringBuilder=new StringBuilder();
            for(Map.Entry<String,GlobalConfig.server> entry:servers.entrySet())
            {//遍历服务器信息
                stringBuilder.append(entry.getKey());
                stringBuilder.append(" ");
                stringBuilder.append(getServerPlayers(entry.getValue().getIp()));
            }
            return  stringBuilder.toString();

        }else if (content.equals("/list"))
        {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("服务器列表：\n");
            for(Map.Entry<String,GlobalConfig.server> entry:servers.entrySet())
            {//遍历服务器信息
                stringBuilder.append(entry.getKey());
                stringBuilder.append("\n");
            }
            return  stringBuilder.toString();
        }else  if(content.startsWith("/server"))
        {
            log.debug(content);
            if(!content.equals("/server"))
            {
                if(servers.containsKey(content.split(" ")[1]))
                {
                    return getServerInfo(servers.get(content.split(" ")[1]).getIp());
                }
            }
            StringBuilder stringBuilder=new StringBuilder();
            for(Map.Entry<String,GlobalConfig.server> entry:servers.entrySet())
            {//遍历服务器信息
                stringBuilder.append(entry.getKey());
                stringBuilder.append(":\n");
                stringBuilder.append(getServerInfo(entry.getValue().getIp()));
            }
            return  stringBuilder.toString();
        }
        else if(content.startsWith("/connect "))
        {
            return getServerInfo(content
                    .replace("/connect ","")
                    .replace(" ",""));
        }else if(ismaga&&content.startsWith("/exec ")){
            String name=content.split(" ")[1];
            TcpTools tools =new TcpTools();
            tools.initTcp(servers.get(name).getIp());
            String res=tools.send(content.replace("/exec ","").replace(name,""),servers.get(name).getPassword());
            tools.closeTcp();
            return res;
        }
        return null;
    }
}
