package com.rainbowsea.springcloud.controller;


import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MemberController {

    @Resource
    private MemberService memberService;


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
        log.info("member-service-provider-10002 save member={}", member);
        int affected = memberService.save(member);
        if (affected > 0) { // 说明添加成功
            return Result.success("添加会员成功", affected);
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
    @GetMapping("/member/get/{id}")
    public Result getMemberById(@PathVariable("id") Long id) {
        Member member = memberService.queryMemberById(id);

        // 模拟超时 ,这里暂停 5秒
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 使用 Result 把查询到的结果返回
        if (member != null) {
            return Result.success("查询会员成功 member-service-provider-10002 ", member);
        } else {
            return Result.error("402", "ID" + id + "不存在 member-service-provider-10002  ");
        }
    }

}
