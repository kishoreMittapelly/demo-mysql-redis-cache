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

import com.optum.web.entity.Post;
import com.optum.web.exception.ResourceNotFoundException;
import com.optum.web.repository.PostRepository;
import com.optum.web.service.PostService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {


    public PostRepository postRepository;
    public RedisTemplate<String, Post> redisTemplate;

    @Override
    public List<Post> findPostById(Long id) {
        List<Post> postList = new ArrayList<>();

        final String key = "post_" + id;
        final ValueOperations<String, Post> operations = redisTemplate.opsForValue();
        //final boolean hasKey = redisTemplate.hasKey(key);
        Set<String> keys = redisTemplate.keys(key+"*");
        if (keys.size()>0) {
            System.out.println("PostServiceImpl----------> Getting values from redis cache >> "+ key);

            //get them one by one
            int count =0;
            for (String key1 : keys) {
                Post value = redisTemplate.opsForValue().get(key1);
                if(count<4){
                    postList.add(value);
                    count++;
                }

            }
        }else{
            final Optional<Post> post = Optional.ofNullable(postRepository.findOne(id));
            if(post.isPresent()) {
                operations.set(key, post.get(), 10, TimeUnit.SECONDS);
                System.out.println("PostServiceImpl----------> Getting values from database >> "+ key);
                postList.add(post.get());
                return postList;
            } else {
                throw new ResourceNotFoundException();
            }
        }
        /*if (hasKey) {
            final Post post = operations.get(key);
            postList.add(post);
            System.out.println("PostServiceImpl.findPostById() : cache post >> " + post.toString());
            return postList;
        }*/
        return postList;

    }

    public Page<Post> getAllPosts(Integer page, Integer size) {

        Page<Post> p=  postRepository.findAll(new PageRequest(page, size));
        for (Post post : p) {
            final String key = "post_" + post.getId();
            final ValueOperations<String, Post> operations = redisTemplate.opsForValue();
            operations.set(key, post);
        }
        System.out.println("PostServiceImpl----------> Getting  All values from database >> ");
        return p;
    }

    @Override
    public Post savePost(Post post) {
        final String key = "post_" + post.getId();
        final ValueOperations<String, Post> operations = redisTemplate.opsForValue();
        operations.set(key, post);
        System.out.println("PostServiceImpl----------> Saved data  in redis  >> "+ redisTemplate.hasKey(key));
        Post postData =  postRepository.save(post);
        System.out.println("PostServiceImpl----------> Saved data  in DATABASE  >> "+ postData.getId());
        return postData;
    }

    @Override
    public Post updatePost(Post post) {
        final String key = "post_" + post.getId();
        final boolean hasKey = redisTemplate.hasKey(key);
        final ValueOperations<String, Post> operations = redisTemplate.opsForValue();
        if (hasKey) {
            redisTemplate.delete(key);
            operations.set(key, post);
            System.out.println("PostServiceImpl----------> updated data in redis >>" + redisTemplate.hasKey(key));
        }
        Post postData =  postRepository.save(post);
        System.out.println("PostServiceImpl----------> Updated DATABASE  >> "+ postData.getId());
        return postData;
    }

    @Override
    public void deletePost(Long id) {
        final String key = "post_" + id;
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            System.out.println("PostServiceImpl----------> Deleted in Redis  >> "+ key);
        }
        final Optional<Post> post = Optional.ofNullable(postRepository.findOne(id));
        if(post.isPresent()) {
            postRepository.delete(id);
            System.out.println("PostServiceImpl----------> Deleted in DATABASE  >> "+ key);
        } else {
            throw new ResourceNotFoundException();
        }
    }

}