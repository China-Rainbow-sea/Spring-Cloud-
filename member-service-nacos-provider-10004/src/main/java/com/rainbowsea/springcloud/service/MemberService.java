package com.rainbowsea.springcloud.service;

import com.rainbowsea.springcloud.entity.Member;

public interface MemberService {

    /**
     * 根据 id 查询 Member
     *
     * @param id
     * @return
     */
    Member queryMemberById(Long id);


    /**
     * 添加 Member
     *
     * @param member
     * @return
     */
    int save(Member member);
}
