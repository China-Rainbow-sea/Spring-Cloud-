# 创建数据库的脚本SQL语句
```
sql

CREATE DATABASE e_commerce_center_db;
USE e_commerce_center_db;
CREATE TABLE member
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `NAME` VARCHAR(64) COMMENT '用户名',
    pwd CHAR(32) COMMENT '密码',
    mobile VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(64) COMMENT '邮箱',
    gender TINYINT COMMENT '性别',
    PRIMARY KEY (id)
);

INSERT INTO member VALUES
(NULL, 'smith', MD5('123'), '123456789000', 'smith@sohu.com', 1);
SELECT * FROM member

```


# 注意事项和细节
1. 如果前端是以表单形式提交了/是以 parameters，则不需要使用@RequestBody，才会进行对象 bean参数的封装，
同时保证 http的请求头的 content-type 是对应
2. 在进行 SpringBoot 应用程序测试时，引入的 JUnit 是 org.junit.jupiter.api.Test
3.在运行程序时，一定要确保你的 XxxMapper.mxl文件被自动放到 target 目录的 classes 指定的目录当中