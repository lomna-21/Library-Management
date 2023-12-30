package com.example.LibraryManagement.DAO;

import com.example.LibraryManagement.DTO.CreateAuthorDTO;
import com.example.LibraryManagement.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("select a from Author a where a.email = :email")
    Author findByEmail(String email);

//    @Query("SELECT NEW com.example.dto.AuthorDTO(a.name, b.name, b.genre) FROM Author a JOIN a.books b")
    //@Query("SELECT NEW com.example.LibraryManagement.DTO.CreateAuthorDTO(a.name, b.name, b.genre) FROM Author a JOIN a.books b")
    @Query("select a from Author a")
    List<CreateAuthorDTO> getAll();

    @Query("SELECT a, b FROM Author a JOIN a.bookList b")
    List<Object[]> findAllAuthorsWithBooks();

    @Query("SELECT a, b FROM Author a JOIN a.bookList b")
    List<Object[]> findAllAuthorsWithBooksRaw();
}