# media-system
一个基于Springboot + Vue + mysql合成的媒体库
## 引用项目
[vue-admin-template](https://github.com/PanJiaChen/vue-admin-template "vue-admin-template")
## 简单介绍
主要用于将本地的影视视频搭建媒体库，并通过豆瓣api构建一个类似emby、plex等的家庭媒体播放平台。
## 使用平台
适用于部署在linux上的媒体库，如需使用在windows平台，则需要修改Springboot中的`com.cosmax.media.system.business.config.WebMvcConfiguration.addResourceHandlers`方法，映射到指定文件夹。
## 视频文件命名规则
| 视频类型 | 规则 | 样例 |
| ------------ | ------------ | ------------ |
| 电影文件 | 影片名.年份.xxxxxxx.后缀 | The.Gamechangers.2015.1080p.HDTV.x264-TASTETV.mkv  |
| 剧集文件 | 文件夹--影片名.S(季度).xxxxxxx.后缀 | Chernobyl.S01E01.1080p.BluRay.DD5.1.x264-playHD |
## 使用方法
### 打包
#### Springboot打包
```
# 清空target
maven clean

# 依据项目生成 jar 文件，application/target下生成lib、application.jar、config目录
maven package
```
#### vue打包
vue模板基于PanJiaChen大佬的[vue-admin-template](https://github.com/PanJiaChen/vue-admin-template "vue-admin-template")
```
# 进入frontend文件夹
cd frontend

# 打包，生成dist文件
npm run build:prod
```
### 部署
将lib、application.jar、config和dist以及doc目录下的media.sql放在服务器的目录下
<Strong>注：服务文件结构</Strong>
```
|-- media
  |-- lib
  |-- config
  |-- dist
  |-- application-0.0.1-SNAPSHOT.jar
  |-- DBDockerfile
  |-- docker-compose.yml
  |-- media.sql
  |-- MediaDockerfile
  |-- nginx.conf
  |-- VueDockerfile
 
```
#### Dockerfile
mysql的Dockerfile
```
# DBDockerfile

FROM mysql:5.7.22
WORKDIR /docker-entrypoint-initdb.d
ADD media.sql media.sql
WORKDIR /
```
Springboot的Dockerfile

```
# MediaDockerfile

FROM java:8
WORKDIR /usr/local/media
VOLUME /usr/local/media
ADD application-0.0.1-SNAPSHOT.jar app.jar
COPY config config
COPY lib lib
EXPOSE 10088
CMD ["java","-jar","app.jar"]
```
Vue的Dockerfile
```
# VueDockerfile

FROM nginx:1.17.9
COPY dist /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/nginx.conf
EXPOSE 80
```
#### Nginx配置文件
```
# nginx.conf

upstream api_server {
    # api 代理服务地址
    server backend:10088;
}
server {
    listen       80;
    server_name  localhost;

    # 匹配 api 路由的反向代理到API服务
    location /api {
      # rewrite  ^.+api/?(.*)$ /$1 break;
      proxy_pass  http://api_server;
      proxy_redirect off;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    # 路由在前端，后端没有真实路由，在路由不存在的 404状态的页面返回 /index.html
    # 这个方式使用场景，你在写React或者Vue项目的时候，没有真实路由
    location / {
      root /usr/share/nginx/html;
      index index.html;
      try_files $uri $uri/ /index.html;
    }
}
```
#### 执行
```
docker-compose up -d
```
### 一些注意
* 本媒体库通过docker内别名通讯，部署在同一个服务器，如过需要部署不同服务器，则注意修改springboot 下配置文件，即`config`目录下文件
* 媒体库同后端链接，默认开启docker内的`/videos`目录，服务器可自由选择文件夹卷积到该目录下，默认为`linux`的`media`目录，如需修改，则修改`docker-compose.yml`