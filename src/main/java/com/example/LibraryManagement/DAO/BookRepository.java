package com.example.LibraryManagement.DAO;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAll();
    List<Book> findByGenre(Genre genre);

    @Query("select b from Book b, Author a where b.my_author.id = a.id and a.name = ?1")
    List<Book> findByAuthor_Name(String authorName);

    @Query("SELECT b FROM Book b WHERE b.name = :bookName")
    Book findByName(String bookName);

    @Query("SELECT b from Book b WHERE b.id = :bookId")
        List<Book> findBookDataById(Integer bookId);
}