package com.rainbowsea.springcloud;


import com.rainbowsea.springcloud.dao.MemberDao;
import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest // 如果指明的路径不同;要加上注解指明(场景启动的所在包路径)// 同时一定要有场景启动器注解
// 如果不想把测试类放到和启动类相同的包下，那就给测试类的注解加上@SpringBootTest(classes = {springbootJpaApplication.class}) 代
@Slf4j
public class MemberApplication10000Test {

    @Resource
    private MemberDao memberDao;

    @Resource
    private MemberService memberService;

    @Test  // 注意选择: org.junit.jupiter.api.Test; 包当中的
    // 注意方法不能定义为 private 私有的，不然无法测试运行的
    public void testQueryMemberById() {
        Member member = memberDao.queryMemberById(1L);
        log.info("member={}", member);
    }


    @Test
    public void testMemberDaosave() {
        Member member = new Member(null, "牛魔王", "123", "1300000", "nmw@shou.com", 1);
        int affected = memberDao.save(member);
        log.info("affected={}", affected);
    }


    @Test  // 注意选择: org.junit.jupiter.api.Test; 包当中的
    // 注意方法不能定义为 private 私有的，不然无法测试运行的
    public void testMemberServiceQueryMemberById2() {
        Member member = memberService.queryMemberById(1L);
        log.info("member={}", member);
    }


    @Test
    public void testMemberServiceSave() {
        Member member = new Member(null, "狐狸精", "123", "1300000", "hlj@shou.com", 2);
        int affected = memberService.save(member);
        log.info("affected={}", affected);
    }
}
