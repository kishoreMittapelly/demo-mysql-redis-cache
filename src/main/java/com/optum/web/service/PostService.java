package com.optum.web.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.optum.web.entity.Post;

public interface PostService {

	List<Post> findPostById(Long id);

    Page<Post> getAllPosts(Integer page, Integer size);
    
    Post savePost(Post post);

    Post updatePost(Post post);

    void deletePost(Long id);
}
