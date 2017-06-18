# ZZC-SERVER 
 基于netty的开源tcp服务器开发包

## 目的&&功能：
快速打造通信服务器，帮助用户快速开发出（游戏，硬件，聊天）服务器
## 特点：
* 依赖少,基于netty和spring等基础库
* 使用户将注意集中与业务开发，无需过多关注配置
* 使用简便，无需懂网络编程和netty也可使用
## 环境依赖：
* jdk1.7+
* maven3.0以上版本

## 如何使用：
* maven引入
```
        <!-- zzc-server -->
        <dependency>
            <groupId>cn.94zichao</groupId>
            <artifactId>server</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
* 编写自己的业务处理类，标记@ZzcService注解
* 业务类中编写对应的各种方法，默认参数为[SocketModel]
```
    @ZzcService("test")
    public class MyTestZzcService {
        public void test(SocketModel sk){
        sk.getData();
        }
    }
```
* 在Spring自动扫描包后面配置服务启动类[ZzcServer]
```   
 <context:component-scan base-package="com.youdomain.xx">
 </context:component-scan>
 <!-- 通信服务器启动类 -->
 <bean id="xxxxxx" class="cn._94zichao.server.bootstrap.ZzcServer">
 </bean>
```
#### 向客户端发送数据的方法：
1. 构建[SocketModel]类，将channelId和字节数据置入
```
    SocketModel sk = new SocketModel();
    sk.setChannelId(xxxxx);
    sk.setData(xxxxx);
```
2. 通过[SocketModel]获得执行类并执行 
```
    ChannelExecutorFactory.getExecutor(sk).exec();
```
[ZzcServer]: ZzcServer.md  "ZzcServer 的启动类"
[SocketModel]: SocketModel.md  "SocketModel 类"
## 教程(未完成，先填个坑)：
1. 与spring集成的demo
2. 与spring boot集成的demo
3. 一个简单的聊天服务器

## 文档（坑先占好）：

## 缺陷（待完善部分）：
1. 项目启动时为了配合公司产品的快速开发，写死了端口、防止粘包拆包的分隔符、eventloop线程数量，后期这些都要可配置化。
2. 未集成日志。
3. 缺少其他防粘包策略，后面提供。
4. 其他高级API
5. 待补充。。。。。。

