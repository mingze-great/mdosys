<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Mdosys v1.0.0</h1> 

<p align="center">参考项目:  
RuoYi:<a href="https://gitee.com/y_project/RuoYi-Cloud/stargazers"><img src="https://gitee.com/y_project/RuoYi-Cloud/badge/star.svg?theme=dark" alt="RuoYi"></a>
Dolphinscheduler:<a href="https://dolphinscheduler.apache.org/en-us/docs/latest/user_doc/about/introduction.html"><img src="https://tokei.rs/b1/github/apache/dolphinscheduler?category=lines" alt="dolphinscheduler"></a>
</p>
---

## 介绍

* :car: 采用前后端分离的模式

* :alien: 后端采用Spring Boot、Spring Cloud & Alibaba。
* :necktie: 注册中心、配置中心选型Nacos，权限认证使用Redis。
* :sleeping: 流量控制框架选型Sentinel，分布式事务选型Seata。
* :earth_africa: 提供了技术栈（[Vue3](https://v3.cn.vuejs.org)| [Element Plus](https://element-plus.org/zh-CN) |[Vite](https://cn.vitejs.dev)）版本[mdosys-ui](https://edu.gitee.com/Geosciences/projects/435253/repos/Geosciences/mdosys-ui/tree/dev)，保持同步更新。


## 快速开始
* 下载maven
* 配置jdk1.8 环境
* 配置idea
* clone项目 `clone https://gitee.com/Geosciences/modsys.git`
* check到feature分支

## 系统模块

~~~
com.modsys     
├── mdosys-gateway         // 网关模块 [8080]
├── mdosys-auth            // 认证中心 [9200]
├── mdosys-api             // 接口模块
│       └── mdosys-api-system                          // 系统接口
├── mdosys-common          // 通用模块
│       ├── mdosys-common-core                         // 核心模块
│       ├── mdosys-common-datascope                    // 权限范围
│       ├── mdosys-common-datasource                   // 多数据源
│       ├── mdosys-common-log                          // 日志记录
│       ├── mdosys-common-redis                        // 缓存服务
│       ├── mdosys-common-seata                        // 分布式事务
│       ├── mdosys-common-security                     // 安全模块
│       └── mdosys-common-swagger                      // 系统接口
├── mdosys-modules         // 业务模块
│       ├── mdosys-modules-system                      // 系统模块 [9201]
│       ├── mdosys-modules-knowledgelib                // 用户文件 [9201]                   
│       └── mdosys-modules-file                        // 文件服务 [9300]
├── mdosys-visual          // 图形化管理模块
│       └── mdosys-visual-monitor                      // 监控中心 [9100]
└── pom.xml                // 公共依赖
~~~
