package com.optum.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.optum.web.entity.Member;
import com.optum.web.exception.ResourceNotFoundException;
import com.optum.web.repository.MemberRepository;
import com.optum.web.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    public MemberRepository memberRepository;
    public RedisTemplate<String, Member> redisTemplate;

    @Override
    public List<Member> findMemberByNameStartingWith(String name) {
        List<Member> memberList = new ArrayList<>();

        final String key = "member_" + name;
        final ValueOperations<String, Member> operations = redisTemplate.opsForValue();
        //final boolean hasKey = redisTemplate.hasKey(key);
        Set<String> keys = redisTemplate.keys(key+"*");
        if (keys.size()>0) {
            System.out.println("MemberServiceImpl----------> Getting values from redis cache >> "+ key);

            //get them one by one
            int count =0;
            for (String key1 : keys) {
                Member value = redisTemplate.opsForValue().get(key1);
                if(count<5){
                    memberList.add(value);
                    count++;
                }
            }
        }else{
        	List<Member> memList = memberRepository.findByNameStartingWith(name);
        	   System.out.println("MemberServiceImpl----------> Getting values from database >> ");
            if(memList.size()>0) {
            	for(Member mem : memList){
                operations.set(key, mem, 10);
            	}
            	memberList.addAll(memList);
                return memberList;
            } else {
                throw new ResourceNotFoundException();
            }
        }
        return memberList;
    }

    public Page<Member> getAllMembers(Integer page, Integer size) {

        Page<Member> mem=  memberRepository.findAll(new PageRequest(page, size));
        for (Member member : mem) {
            final String key = "member_" + member.getMemberName();
            final ValueOperations<String, Member> operations = redisTemplate.opsForValue();
            operations.set(key, member);
        }
        System.out.println("MemberServiceImpl----------> Getting  All values from database and save the data in Redis >> ");
        return mem;
    }

    @Override
    public Member saveMember(Member member) {
        final String key = "member_" + member.getMemberName();
        final ValueOperations<String, Member> operations = redisTemplate.opsForValue();
        operations.set(key, member);
        System.out.println("MemberServiceImpl----------> Saved data  in redis  >> "+ redisTemplate.hasKey(key));
        Member memberData =  memberRepository.save(member);
        System.out.println("MemberServiceImpl----------> Saved data  in DATABASE  >> "+ memberData.getId());
        return memberData;
    }

    @Override
    public Member updateMember(Member member) {
        final String key = "member_" + member.getMemberName();
        final boolean hasKey = redisTemplate.hasKey(key);
        final ValueOperations<String, Member> operations = redisTemplate.opsForValue();
        if (hasKey) {
            redisTemplate.delete(key);
            operations.set(key, member);
            System.out.println("MemberServiceImpl----------> updated data in redis >>" + redisTemplate.hasKey(key));
        }
        Member memberData =  memberRepository.save(member);
        System.out.println("MemberServiceImpl----------> Updated DATABASE  >> "+ memberData.getId());
        return memberData;
    }

    @Override
    public void deleteMember(Long id) {
        final Optional<Member> member = Optional.ofNullable(memberRepository.findOne(id));
        if(member.isPresent()) {
        	  final String key = "member_" + member.get().getMemberName();
              final boolean hasKey = redisTemplate.hasKey(key);
        	  if (hasKey) {
                  redisTemplate.delete(key);
                  System.out.println("MemberServiceImpl----------> Deleted in Redis  >> "+ key);
              }
            memberRepository.delete(id);
            System.out.println("MemberServiceImpl----------> Deleted in DATABASE  >> "+ key);
        } else {
            throw new ResourceNotFoundException();
        }
    }

	@Override
	public Member findMemberById(Long id) {
		return memberRepository.findOne(id);
	}


}