package cn.qaq.valveqqrobot.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @program: qqrobota2srcon
 * @description: 配置加载器
 * @author: QAQ
 * @create: 2019-11-20 09:16
 **/
@Component
@Slf4j
@Getter
public class GlobalConfig {

    private LinkedHashMap<String,server> serverMap=new LinkedHashMap<String,server>();//
    @Value("#{'${config.server.qq}'.split(';')}")
    private List<String> manaQQs=new ArrayList<>();//这个是管理员qq号，为空则无需授权使用rcon命令

    @Value("#{'${config.server.ip}'.split(';')}")
    private List<String> ips;

    @Value("#{'${config.server.password}'.split(';')}")
    private List<String> passwords;
    @Value("#{'${config.server.name}'.split(';')}")
    private List<String> names;
    @Value("#{'${config.server.group}'.split(';')}")
    private List<String> qqGroup;
    @Value("${config.server.groupflag}")
    private boolean groupFlag; //默认false
    @Value("${config.server.log}")
    private String logflag;
    @Value("${config.server.qqnumber}")
    private String qqnumber;
    @Value("${config.server.qqpasswd}")
    private String qqpasswd;
    @Autowired
    private ConfigurableApplicationContext context;

    public LinkedHashMap<String, server> getServerMap() {
        return serverMap;
    }

    //true则响应，false则不响应
    public boolean isQQenable(String group)
    {
        if(group==null) return true;
        if(!groupFlag||qqGroup==null||qqGroup.size()==0) return true;
        if(qqGroup.contains(group)) return true;
        return false;
    }
    public class server
    {
        private String ip;
        private String password;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public server(String ip, String password) {
            this.ip = ip;
            this.password = password;
        }
    }
    public server getServer(String name)
    {
        return  serverMap.get(name);
    }
    public boolean isAuth(String qq)
    {
        if(manaQQs==null||manaQQs.size()==0) return true;
        else return manaQQs.contains(qq);
    }
    @PostConstruct
    public void Init()
    {
        log.info("准备加载配置文件信息.....");
        try {
            for(Integer i=0;i<ips.size();i++)
            {
                log.debug(names.get(i));
                if(serverMap.containsKey(names.get(i))) throw new Exception("加载配置错误！不应存在相同的服务器名称");
                serverMap.put(names.get(i),new server(ips.get(i),passwords.get(i)));
            }
            log.info("配置加载成功....已注入"+serverMap.size()+"个服务器信息");
            if(groupFlag) log.info("QQ群白名单已开启");
            if(qqpasswd==null || qqpasswd.equals("")) {
                log.error("qq密码禁止为空");
                context.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("初始化配置出现异常！请检查配置文件");
            e.printStackTrace();
            context.close();
        }
    }
}
