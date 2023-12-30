package com.example.LibraryManagement.DAO;


import com.example.LibraryManagement.Model.Transaction;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionCacheRepository {

    @Autowired
    RedisTemplate<String ,Object> redisTemplate;

    public Transaction get(Integer transactionId){
        Object result = redisTemplate.opsForValue().get(getKey(transactionId));
        return (result == null) ? null :  (Transaction) result;
    }

    public void set(Transaction transaction){
        redisTemplate.opsForValue().set(getKey(transaction.getId()), transaction);
    }

    private String getKey(Integer transactionId){
        return Constants.TRANSACTION_CACHE_KEY_PREFIX + transactionId;
    }
}
