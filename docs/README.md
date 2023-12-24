# 项目信息
## 后端环境
### 数据库

数据库使用的是 Postgresql15，可以使用 Docker 安装，项目中默认配置如下：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://vm2:5432/db_carryyou
    username: postgres
    password: secret
    driver-class-name: org.postgresql.Driver
```

数据库安装完成后创建数据库，使用 carryyou_backend/db/ddl  及 carryyou_backend/db/dml  下的脚本初始化数据库。

### JDK

项目基于 JDK1.8.2，需提前安装 OpenJDK1.8。

### Maven

项目基于 Maven 3.8.1，阿里云仓库。

### Yolo

项目默认基于 Yolov4 预训练模型，如要替换为自己训练的模型，导出自己训练的模型的配置文件、权重文件，放到 carryyou-backend/src/main/resources/yolo 目录下，同时修改 application.yml 中关于这模型文件的配置。

```yaml
yolo:
  yolo-cfg-path: /Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/yolov4.cfg
  yolo-weights-path: /Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/yolov4.weights
  yolo-coconames-path: /Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/coco.names
  yolo-width: 608
  yolo-height: 608
```

### 图片保存路径

启动项目前需先修改图片保存路径的配置：

```yaml
image:
  base:
    path: ~/carryyou/carryyou-backend/images/
```

改为本地存在的路径。因为没有文件服务，所以查看图片只能下载后在本地查看，不能在线预览。	

图片按照 年 / 月 / 检测类型（品种/成熟度）分三级目录保存。

## 前端环境

### Node.js

前端基于 v18.15.0 ，需配置好淘宝镜像仓库：`npm config set registry https://registry.npm.taobao.org/`

进入项目后执行 `npm install` 进行安装，`npm run dev` 启动前端项目，使用默认用户登陆。

默认用户：

| 用户id | 密码    | 用户状态 | 角色     |
| ------ | ------- | -------- | -------- |
| U0001  | 1234561 | 正常     | 管理员   |
| U0002  | 1234562 | 正常     | 管理员   |
| U0003  | 1234563 | 正常     | 普通用户 |
| U0004  | 1234564 | 正常     | 普通用户 |
| U0005  | 1234565 | 注销     | 普通用户 |

### Vue.js

基于 @vue/cli 5.0.8 。

## 功能介绍

### 用户角色

简单分了两个角色：

| 角色编码   | 角色名称   | 权限                                                         |
| ---------- | ---------- | ------------------------------------------------------------ |
| ROLE_ADMIN | 系统管理员 | 可以新增/删除/修改所有用户，可以删除图片文件                 |
| ROLE_USER  | 普通用户   | 只能上传图片进行检测并下载检测图片，可以查看全部用户但不能删除和修改，仅可在“我的 - 个人信息”页面修改自己的信息。 |

### 图片列表页面

仅支持 jpg/jpeg 格式的图片，上传前需选择识别类型，图片上传成功后会自动检测，检测完成后可从图片文件列表页面点击“查看识别结果”下载识别后的图片进行查看（因为前端组件不支持本地绝对路径预览图片，所以只支持下载后查看）。

识别后的图片文件和原文件保存在同一目录下，目标检测生成的图片由后缀“_dec”进行区分，这个后缀可以在配置文件中修改：

```yaml
image:
  detect:
    suffix: _dec
```

**管理员用户可以查看所有用户上传的图片并进行删除（会一同删除磁盘文件），普通用户只能查看自己上传的图片，不能进行删除。**

### 用户列表页面

**管理员用户可以查看/修改/删除所有用户的信息，普通用户可以查看所有用户的信息，但不能进行新增/删除/修改。**删除操作不删除数据库记录，仅将用户状态字段更新为“删除”，删除后用户无法登陆。

### 我的个人信息

此页面可以查看当前登陆用户的信息，可以更新 用户名/密码 字段，可以进行账户注销，注销操作仅将用户状态字段更新为“注销”，注销后无法再进行登陆。

### token 时效

考虑到用户较少，简单起见，将 token 列表保存到了系统内存中，由定时任务：ClearExpiredTokenJob 每分钟清理一遍过期 token，拦截器 TokenInterceptor 会校验除注册/登陆以外的请求中携带的 token，如 token 超期，需要重新登陆。token 默认有效时长为 15 分钟，可在配置文件中修改：

```yaml
token:
  expire:
    after:
      minutes: 15
```

*内存中的 token 信息每次重启都会被清空。*

