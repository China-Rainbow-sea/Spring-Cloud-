package com.rainbowsea.springcloud.dao;

import com.rainbowsea.springcloud.entity.Member;
import org.apache.ibatis.annotations.Mapper;


@Mapper  // 标注注解被扫描到,或是在 配置类/场景启动项中 @MapperScan(指明扫描路径)
public interface MemberDao {

    // 定义方法
    // 根据 id 返回 member 数据
    Member queryMemberById(Long id);

    /**
     * 添加 member
     *
     * @param member
     * @return
     */
    int save(Member member);
}
