package com.optum.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.optum.web.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>  {
    Page<Member> findAll(Pageable pageable);
    @Query("select m from Member m where m.memberName like %?1%")
    List<Member> findByNameStartingWith(String name);
    

} 
