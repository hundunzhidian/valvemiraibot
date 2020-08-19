# 根据vavle A2a查询协议定制的qq机器人


## 声明

### 一切开发旨在学习，请勿用于非法用途
- 机器人使用了[mirai](https://github.com/mamoe/mirai)核心协议
- A2S游戏查询、Rcon通讯协议为[valve公司](https://www.valvesoftware.com/)所属
- 项目由[qqrobota2srcon](https://github.com/hundunzhidian/qqrobota2srcon)发展而来，适配于mirai机器人框架
- mirai 是完全免费且开放源代码的软件，仅供学习和娱乐用途使用
- mirai 不会通过任何方式强制收取费用，或对使用者提出物质条件
- mirai 由整个开源社区维护，并不是属于某个个体的作品，所有贡献者都享有其作品的著作权。


### 许可证

**qq机器人协议原版权归属腾讯科技股份有限公司所有，本项目其他代码遵守**：  
[**GNU AFFERO GENERAL PUBLIC LICENSE version 3 with Mamoe Exceptions**](https://github.com/mamoe/mirai/blob/master/LICENSE) （简称 `AGPLv3 with Mamoe Exceptions`）, 建立在 [**GNU AFFERO GENERAL PUBLIC LICENSE version 3**](https://www.gnu.org/licenses/agpl-3.0.html) （简称 `AGPLv3`）的基础之上添加额外条件。

如果与 `AGPLv3` 冲突，则以 `AGPLv3 with Mamoe Exceptions` 的如下额外条件为准。

- **所有衍生软件 *(衍生软件: 间接或直接接触到 mirai, 即使没有修改 mirai 源码的软件)* 必须使用相同协议 (AGPLv3 with Mamoe Exceptions) 开源**
- **本软件禁止用于一切商业活动**
- **本软件禁止收费传递, 或在传递时不提供源代码**

### 配置文件说明
#### 服务器ip,以;隔开
```shell
config.server.ip=127.0.0.1:27015;127.0.0.1:27016
```
##### 服务器密码,不要使用特殊字符,以;隔开,跟上面ip对应
```shell
config.server.password=aaa;bbb
```
##### 服务器名称,英文或数字，不要使用特殊字符，以;隔开，跟上面ip一一对应
```shell
config.server.name=server1;server2
```
##### 管理员qq号,数字,以;隔开
```shell
config.server.qq=
```
##### 是否开启qq群白名单,false为不开启白名单，true为开启
```shell
config.server.groupflag=false
```
##### qq群白名单，其他群都不会进行响应，以;隔开
```shell
config.server.group=
```
### 是否开启miraibot启动日志，值有 net,bot,all,no，net只打印网络日志,bot打印通讯日志，all打印所有，no关闭所有，建议第一次启动的时候开启，需要进行设备锁验证，后面没有问题的话可以填写no关闭所有日志
```shell
config.server.log=no
```
##### 登录的qq号
```shell
config.server.qqnumber=
```
##### 登录的qq密码
```shell
config.server.qqpasswd=
```

## 使用方法

### 准备

   -  基于springboot构建完成，所以需要JDK1.8及以上版本，文下的jar文件均指编译打包后的jar包，若想自己编译可以使用maven工具构建。
    然后将源码目录下src/main/resources/ 或者release压缩包目录下的application.properties放到跟jar包同一个目录下
    
### 运行
 -    在控制台输入
```shell
java -jar valveqqrobot-0.0.1-SNAPSHOT.jar
```
  -   对于windows机器，必须输入
```shell
java -Dfile.encoding=utf-8 -jar valveqqrobot-0.0.1-SNAPSHOT.jar
```
    
   -  第一次运行前必须将上文配置application.properties内的config.server.log配置项更改为all，否则控制台看不到验证url或者验
    证码，如果需要输入验证码，直接在控制台输入然后回车即可，若需要扫描二维码， 将网址复制到浏览器使用手机扫描并确认然后返回控制台回车，登录第一次后会随机在同路径生成一个device.json设备描述文件，再登录几次后无需再使用二维码或者验证码登录，即可关
    闭log输出
