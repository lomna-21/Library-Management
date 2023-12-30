package com.example.LibraryManagement.DAO;


import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookCacheRepository {

    @Autowired
    RedisTemplate<String ,Object> redisTemplate;

    public Book get(Integer bookId){
        Object result = redisTemplate.opsForValue().get(getKey(bookId));
        return (result == null) ? null :  (Book) result;
    }

    public void set(Book book){
        redisTemplate.opsForValue().set(getKey(book.getId()), book);
    }

    private String getKey(Integer bookId){
        return Constants.BOOK_CACHE_KEY_PREFIX + bookId;
    }
}
