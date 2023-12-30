package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DAO.AuthorCacheRepository;
import com.example.LibraryManagement.DAO.AuthorRepository;
import com.example.LibraryManagement.DTO.CreateAuthorDTO;
import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorCacheRepository authorcacheRepository;

    public Author getOrCreate(Author author){

        Author authorRetrieved = authorRepository.findByEmail(author.getEmail());
        if(authorRetrieved == null) {
            authorRetrieved = authorRepository.save(author);
        }
        return authorRetrieved;
    }

    public List<CreateAuthorDTO> getAllAuthors(){

        return convertToDTO(authorRepository.findAllAuthorsWithBooksRaw());
    }

    public Author find(Integer authorId) {

        Author author =authorcacheRepository.get(authorId);
        if(author != null){
            return author;
        }
        author = authorRepository.findById(authorId).orElse(null);
        if(author != null){
            authorcacheRepository.set(author);
        }
        return author;
    }

    public List<CreateAuthorDTO> convertToDTO(List<Object[]> rawData) {
        return rawData.stream().map(data -> {
            Author author = (Author) data[0];
            Book book = (Book) data[1];
            return new CreateAuthorDTO(author.getName(), author.getEmail(), book.getName(), book.getGenre());
        }).collect(Collectors.toList());
    }


}