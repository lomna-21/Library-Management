package com.example.LibraryManagement.DAO;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminCacheRepository {

    @Autowired
    RedisTemplate<String ,Object> redisTemplate;

    public Admin get(Integer adminId){
        Object result = redisTemplate.opsForValue().get(getKey(adminId));
        return (result == null) ? null :  (Admin) result;
    }

    public void set(Admin admin){
        redisTemplate.opsForValue().set(getKey(admin.getId()), admin);
    }

    private String getKey(Integer adminId){
        return Constants.ADMIN_CACHE_KEY_PREFIX + adminId;
    }
}
