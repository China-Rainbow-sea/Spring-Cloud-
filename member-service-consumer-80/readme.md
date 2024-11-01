# RestTemplate 是 Spring 提供的用于访问 Rest 服务的模块类
1. RestTemplate 提供了多种便捷**访问远程Http服务** 的方法
2. 说明: 大家可以这样理解，通过 RestTemplate ，我们可以发出 Http请求(支持Restful风格)，
去调用Controller 提供的 API接口，就像我们使用浏览器发出http请求，调用该 API 接口一样。
3. 使用简单便捷

对应的官方地址: https://docs.spring.io/spring-framework/docs/6.0.11/javadoc-api/org/springframework/web/client/RestTemplate.html


# 注意 微服务组件之间通信的的一个坑点 @RequestBody
注意：微服务组件通信的坑点:
这里我们使用 RestTemplate 传输发送的数据格式是以 json 格式的所以要添加撒谎给你 @RequestBody
将json 格式的字符串转换为 bean对象进行赋值
同时，我们 bean 对象传输过程中是需要序列化的

# 对象流 ObjectInputStream 和 ObjectOutPutStream
看一个需求:
1. 将 int num = 100 这个int 数据保存到文件中，注意不是 100 数字，而是 int 100,并且能够从
文件中直接恢复成 int 100
2. 将 Dog dog = new Dog("小黄",3) 这个 dog 对象保存到文件中，并且能够从文件中恢复成 Dog dog
3. 上面的要求，就是能够将基本数据类型或者对象进行“序列化” 和 “反序列化”

## 序列化 和 反序列化
1.序列化就是在保存数据时，保存数据的值和数据类型
2.反序列化就是在恢复数据时，恢复数据的值和数据类型
3.需要让某个对象，支持序列化，则必须让其类是序列化的，为了让某个类是可序列化的，则
该类必须实现如下“两个接口”的其中一个
* Serializable // 这是一个标记接口，没有方法
* Externalizable // 该接口有方法需要实现，因此我们一般实现上面的 Serializable 接口，即可。


# Run DashBoard 介绍