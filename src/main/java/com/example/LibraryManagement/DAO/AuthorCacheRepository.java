package com.example.LibraryManagement.DAO;


import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorCacheRepository {

    @Autowired
    RedisTemplate<String ,Object> redisTemplate;

    public Author get(Integer authorId){
        Object result = redisTemplate.opsForValue().get(getKey(authorId));
        return (result == null) ? null :  (Author) result;
    }

    public void set(Author author){
        redisTemplate.opsForValue().set(getKey(author.getId()), author);
    }

    private String getKey(Integer authorId){
        return Constants.AUTHOR_CACHE_KEY_PREFIX + authorId;
    }
}
