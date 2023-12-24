# 项目信息
## 后端环境准备
### 数据库

数据库使用的是 Postgresql15，可以使用 Docker 安装，项目中默认配置如下：

```properties
spring:
  datasource:
    url: jdbc:postgresql://vm2:5432/db_carryyou
    username: postgres
    password: secret
    driver-class-name: org.postgresql.Driver
```

### Docker

可选。

### JDK

项目基于 JDK1.8.2，需提前安装 OpenJDK1.8。

### Maven

项目基于 Maven 3.8.1，

## 前端环境准备
### Node.js
### Vue.js
## 功能介绍
