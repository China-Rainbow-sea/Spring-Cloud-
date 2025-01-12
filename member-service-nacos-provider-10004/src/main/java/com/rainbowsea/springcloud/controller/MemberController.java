package com.rainbowsea.springcloud.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.handler.CustomGlobalBlockHandler;
import com.rainbowsea.springcloud.handler.CustomGlobalFallbackHandler;
import com.rainbowsea.springcloud.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MemberController {

    private static int num = 0;  // 执行的计数器-static静态
    @Resource
    private MemberService memberService;


    // 这里我们使用全局限流处理类，显示限流信息

    /**
     * value="t6" 表示 sentinel 限流资源的名字
     * blockHandlerClass = CustomGlobalBlockHandler.class:全局限流处理类
     * blockHandler = "handlerMethod1" 指定使用全局限流处理类哪个方法，来处理限流信息
     * fallbackClass = CustomGlobalFallbackHandler.class 全局 fallback处理类
     * fallback = "fallbackHandlerMethod1" 指定使用全局fallback处理类哪个方法来处理java异常/业务异常
     * exceptionsToIgnore = {NullPointerException.class}
     * @return
     */
    @GetMapping("/t6")
    @SentinelResource(value = "t6",
            //   //设置处理sentinel 控制台违规后的异常 blockHand
            blockHandlerClass = CustomGlobalBlockHandler.class,
            blockHandler = "handlerMethod1",
    //设置处理Java异常的 fallback
            fallbackClass = CustomGlobalFallbackHandler.class,
            fallback = "fallbackHandlerMethod1",

            // 如果希望忽略某个异常，可以使用 exceptionsToIgnore，这里忽略NullPointerException异常
            exceptionsToIgnore = {NullPointerException.class}
    )
    public Result t6() {
        log.info("执行t6() 线程id={}", Thread.currentThread().getId());


        // 假定；当访问t6资源次数是5倍数时，就出现Java异常
        if (++num % 5 == 0) {
            throw new NullPointerException("null 指针异常 num=" + num);
        }

        if (++num % 6 == 0) {
            throw new RuntimeException("RuntimeException  num=" + num);
        }
        return Result.success("200", "t6()执行OK~~~");
    }


    /**
     * 解读
     *
     * @param id
     * @param type
     * @return
     */
    // 完成对热点key限流的测试 /new?id=x&type=x
    @GetMapping("/news")
    @SentinelResource(value = "news", blockHandler = "newsBlockHandler")
    public Result queryNews(@RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "type", required = false) String type) {

        // 在实际开发中，新闻应该到DB或者缓存当中获取，老师这里就模拟
        log.info("到DB 查询新闻");
        return Result.success("返回id=" + id + "新闻 from DB");
    }

    // 热点key限制/限流异常处理方法
    public Result newsBlockHandler(String id, String type, BlockException blockException) {

        //return Result.success("查询id="+id + "新闻触发热点key限流保护 sorry..");
        return Result.error("401", "查询id=" + id + "新闻触发热点key限流保护 sorry..");
    }


    @GetMapping("/t1")
    public Result t1() {
        return Result.success("t1执行成功");
    }


    @GetMapping("/t2")
    public Result t2() {

        // 让线程休眠 1s，模拟执行时间为1s=>当多少个请求就会造成超时
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出线程信息
        log.info("执行t2()，线程id={}", Thread.currentThread().getId());
        return Result.success("t2执行成功");
    }


    @GetMapping("/t3")
    public Result t3() {
        try {
            // 让线程休眠300毫秒，模拟执行时间
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success("t3()执行成功");
    }


    @GetMapping("/t4")
    public Result t4() {

        // 设计异常比例达到50%
        if (++num % 2 == 0) {
            // 制造一个异常
            System.out.println(3 / 0);
        }

        log.info("熔断降级测试[异常比例] 执行t4() 线程id={}", Thread.currentThread().getId());

        return Result.success("t4()执行...");
    }


    @GetMapping("/t5")
    public Result t5() {

        // 设计异常比例达到50%
        // 出现10次异常，这里需要设置大于6，需要留出几次做测试和加入簇点链路
        if (++num <= 10) {
            // 制造一个异常
            System.out.println(3 / 0);
        }

        log.info("熔断降级测试[异常比例] 执行t4() 线程id={}", Thread.currentThread().getId());

        return Result.success("t5()执行...");
    }



    /*
    说明:
         1. 我们的前端如果是以 json 格式来发送添加信息的Member，那么我们需要使用 @RequestBody
         才能将数据封装到对应的 bean，同时保证http的请求的 content-type 是对应
         2. 如果前端是以表单形式提交了，则不需要使用@RequestBody,才会进行对象bean参数封装，同时
         保证 http的请求的 content-type 是对应
     */

    /**
     * 添加方法/接口
     *
     * @param member
     * @return
     */
    @PostMapping("/member/save")
    public Result save(@RequestBody Member member) {
        // 注意：微服务组件通信的坑点:
        // 这里我们使用 RestTemplate 传输发送的数据格式是以 json 格式的所以要添加撒谎给你 @RequestBody
        // 将json 格式的字符串转换为 bean对象进行赋值
        // 同时，我们 bean 对象传输过程中是需要序列化的。
        log.info("member-service-provider-10000 save member={}", member);
        int affected = memberService.save(member);
        if (affected > 0) { // 说明添加成功
            return Result.success("添加会员成功 member-service-nacos-provider-10004 ", affected);
        } else {
            return Result.error("401", "添加会员失败");
        }
    }


    /**
     * 这里我们使用 url占位符 + @PathVariable
     *
     * @param id
     * @return
     */
    //@GetMapping("/member/get/{id}")
    // 在sentinel中  /member/get?id=1  和  /member/get?id=2  被统一认为是 /member/get  所以只要对/member/get  限流就OK了. 进行统一的限流
    //@RequestMapping(value = "/member/get/", params = "id", method = RequestMethod.GET)
    //public Result getParameter(Long id) {
    @GetMapping("/member/get/{id}")
    public Result getMemberById(@PathVariable("id") Long id, HttpServletRequest request) {
        Member member = memberService.queryMemberById(id);
        String color = request.getParameter("color");
        String age = request.getParameter("age");


        // 让线程休眠1s，模拟执行时间
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 模拟超时 ,这里暂停 5秒
       /* try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        System.out.println(" enter getMemberById... 当前线程id = " + Thread.currentThread().getId() + "时间 = " + new Date());

        // 使用 Result 把查询到的结果返回
        if (member != null) {
            return Result.success("查询会员成功 member-service-nacos-provider-10004  color" + color + "age" + age, member);
            //return Result.success("查询会员成功 member-service-nacos-provider-10004  color",member);
        } else {
            return Result.error("402", "ID" + id + "不存在 member-service-nacos-provider-10004 ");
        }
    }

}
