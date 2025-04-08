**🌐 Echo云盘**

一个小型网盘项目。



**📚 应用场景**

**项目学习，课程设计，简历制作，个人文件存储管理等。**



**📁 项目结构**

```
echopan/
├── echopan-front     # 前端项目
└── echopan-java      # 后端项目
```



**🚀 技术栈**

前端：Vue3 + Vite3 + Vuex + Axios + Pinia + Js + Element Plus + Vue-clipboard3 + iconfont.cn

后端：Spring Boot + Mybatis + MySQL + Redis + ffmpeg



**🧰 开发工具**

💻 IDE：IntelliJ IDEA、Visual Studio Code

🔧 构建工具：Maven、Vite

🐾 接口调试：Postman / Apifox

🐙 版本控制：Git + GitHub



**🧠 项目特点**

- **文件上传功能**：文件分片MD5、大文件切片上传、文件秒传、视频和图片文件转码等功能。

- **文件分享功能**：生成分享链接，进行文件分享。

- **文件回收站**：文件删除后会进入回收站，可以恢复或者彻底删除。

- **文件预览功能**：实现了常见功能的预览，包括：文档文件（docx, pdf, excel）、代码文件、图片文件、视频文件、音频文件。



## 项目预览

**登录页**

![1](https://yl-framework.oss-cn-heyuan.aliyuncs.com/Snipaste_2025-04-04_11-29-14.png)

**首页**

![2](https://yl-framework.oss-cn-heyuan.aliyuncs.com/Snipaste_2025-04-04_11-31-36.png)

**图片预览**

![3](https://yl-framework.oss-cn-heyuan.aliyuncs.com/Snipaste_2025-04-04_11-31-40.png)


## 项目部署

### 前端启动

#### 依赖安装
```
npm install
```

#### 编译运行
```
npm run dev
```

#### 编译打包
```
npm run build
```


### 后端启动

1、使用 MySQL 连接工具，创建一个 echopan 架构，工具里面启动命令行选中架构，运行 `echopan.sql` 脚本文件，完成 MySQL 表初始化

2、在`application.properties`配置 `Mysql` 的连接地址和密码和架构选择

3、在`application.properties`配置 `redis` 连接地址和账号密码

4、在`application.properties`配置 `本地/服务器` 资源存储路径
