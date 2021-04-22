package com.optum.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.optum.web.entity.Member;
import com.optum.web.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/members")
@Api(tags = { "members" })
public class MemberController extends AbstractRestHandler {
	@Autowired
	public MemberService memberService;

	@GetMapping("/getMembers")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get  Members.", notes = "You have to provide a valid Member Name.")
	public List<Member> findMembersByName(
			@ApiParam(value = "The NAME of the realted Mmeber.", required = true) @RequestParam("name") String name) {
		  long startTime = System.currentTimeMillis();
		System.out.println("MemberController----------> findMembersByName  START++  startTime :  "+startTime);
		List<Member> memberList = memberService.findMemberByNameStartingWith(name);
		List<Member> list = !memberList.isEmpty() ? memberList : new ArrayList<Member>();
		System.out.println("MemberController----------> findMembersByName  END ++ duration  "+(System.currentTimeMillis() - startTime)+"ms");
		return list;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get a paginated list of all Members.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
	public @ResponseBody Page<Member> getAllMembers(
			@ApiParam(value = "The page number (zero-based)", required = true) @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@ApiParam(value = "Tha page size", required = true) @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
		 long startTime = System.currentTimeMillis();
		System.out.println("MemberController----------> getAllMembers  START++  startTime :  "+startTime);
		Page<Member> memberList = memberService.getAllMembers(page, size);
		System.out.println("MemberController----------> getAllMembers  END ++ duration  "+(System.currentTimeMillis() - startTime)+"ms");
		return memberList;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a member resource.", notes = "Returns the URL of the new resource in the Location header.")
	public void createMember(@RequestBody Member member, HttpServletRequest request, HttpServletResponse response) {
		 long startTime = System.currentTimeMillis();
		System.out.println("MemberController----------> createMember  START++  startTime :  "+startTime);
		Member createMember = memberService.saveMember(member);
		response.setHeader("Location", request.getRequestURL().append("/").append(createMember.getId()).toString());
		System.out.println("MemberController----------> createMember  END ++ duration  "+(System.currentTimeMillis() - startTime)+"ms");
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update a member resource.", notes = "You have to provide a valid member ID in the URL and in the payload. The ID attribute can not be updated.")
	public void updateMember(
			@ApiParam(value = "The ID of the existing member resource.", required = true) @PathVariable("id") Long id,
			@RequestBody Member member) {
		 long startTime = System.currentTimeMillis();
		System.out.println("MemberController----------> updateMember  START++  startTime :  "+startTime);
		checkResourceFound(this.memberService.findMemberById(id));
		memberService.updateMember(member);
		System.out.println("MemberController----------> updateMember  END ++ duration  "+(System.currentTimeMillis() - startTime)+"ms");
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete a member resource.", notes = "You have to provide a valid member ID in the URL. Once deleted the resource can not be recovered.")
	public void deleteMember(
			@ApiParam(value = "The ID of the existing member resource.", required = true) @PathVariable("id") Long id) {
		 long startTime = System.currentTimeMillis();
		System.out.println("MemberController----------> deleteMember  START++  startTime :  "+startTime);
		checkResourceFound(this.memberService.findMemberById(id));
		memberService.deleteMember(id);
		System.out.println("MemberController----------> deleteMember  END ++ duration  "+(System.currentTimeMillis() - startTime)+"ms");
	}

}