package com.rainbowsea.springcloud.controller;

import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@RestController
@Slf4j
public class MemberConsumerController {


    // 定义 MEMBER_SERVICE_PROVIDER_URL 这是一个基础 url地址
    // 使用 shift+ctrl+u 进行字母大小写的切换
    private static final String MEMBER_SERVICE_PROVIDER_URL = "http://localhost:10000";


    // 装配 RestTemplate bean/对象
    @Resource
    private RestTemplate restTemplate;


    @PostMapping("/member/consumer/save")
    public Result<Member> save(Member member) {
        // 1.第1个参数: 就是请求的完整的url:MEMBER_SERVICE_PROVIDER_URL + "/member/save" => http://localhost:10000/member/save
        // 表示你向将该请求，发送给那个微服务处理，注意无论是返回值，还是参数， @PostMapping() 请求方式都要一一对应上对应处理的微服务上的内容
        //2. 第2个参数: member : 就是通过 restTemplate 发出的 post 请求携带数据或者对象
        //3. 第3个参数: Result.class ，微服务当中的对应处理的方法的，返回值，也就是返回对象类型
        // 注意：坑点
        // 这里通过 restTemplate 调用服务模块的接口，就是一个远程调用
        //
        log.info("member-service-consumer-80 save member={}", member);
        return restTemplate.postForObject
                (MEMBER_SERVICE_PROVIDER_URL + "/member/save", member, Result.class);

    }

    /*
    1.与
     @PostMapping("/member/save")
    public Result save(Member member) {
        int affected = memberService.save(member);
        if (affected > 0) { // 说明添加成功
            return Result.success("添加会员成功", affected);
        } else {
            return Result.error("401", "添加会员失败");
        }
    }
     */


    /**
     * 方法/接口，调用服务接口，返回 member 对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/member/consumer/get/{id}")
    public Result<Member> getMemberById(@PathVariable("id") Long id) {

        // 这里就用两个参数
        // 第一个参数，因为是查询，所以这里我们直接字符串拼接上去
        // 这里通过
        return restTemplate.getForObject(MEMBER_SERVICE_PROVIDER_URL + "/member/get/" + id, Result.class);
    }

    /*
    保持一致：
      * 提交方式
      * 返回类型
      * 参数
      * 路径映射要对应上
     @GetMapping("/member/get/{id}")
    public Result getMemberById(@PathVariable("id") Long id) {
        Member member = memberService.queryMemberById(id);

        // 使用 Result 把查询到的结果返回
        if (member != null) {
            return Result.success("查询会员成功", member);
        } else {
            return Result.error("402", "ID" + id + "不存在");
        }
    }

     */
}
