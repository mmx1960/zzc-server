# ZZC-SERVER 
## 基于netty的开源tcp服务器开发包
===== 
### 目的：
快速打造通信服务器，帮助用户快速开发出（游戏，硬件，聊天）服务器
### 特点：
* 依赖少 基于netty和spring等基础库
* 使用户将注意集中与业务开发，无需过多关注配置
* 使用简便，无需懂网络编程和netty也可使用
### 如何使用：
* maven引入
* 编写自己的业务处理类，标记@ZzcService注解
* 业务类中编写对应的各种方法，默认参数为[SocketModel]
* 在Spring自动扫描包后面配置服务启动类[ZzcServer]
 
[ZzcServer]: http://example.com/  "ZzcServer 的启动类"
[SocketModel]: http://example.com/  "SocketModel 类"
### 教程：
1. 与spring集成的demo
2. 与spring boot集成的demo
3. 一个简单的聊天服务器

