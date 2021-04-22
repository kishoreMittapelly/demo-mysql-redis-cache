package com.optum.web.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.optum.web.entity.Member;

public interface MemberService {

	List<Member> findMemberByNameStartingWith(String name);
	
	Member findMemberById(Long id);

    Page<Member> getAllMembers(Integer page, Integer size);
    
    Member saveMember(Member member);

    Member updateMember(Member member);

    void deleteMember(Long id);
}
