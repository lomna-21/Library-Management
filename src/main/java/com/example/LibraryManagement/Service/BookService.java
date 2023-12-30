package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DAO.BookCacheRepository;
import com.example.LibraryManagement.DAO.BookRepository;

import com.example.LibraryManagement.DTO.FindBookDTO;
import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class BookService {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookCacheRepository bookcacheRepository;


    public void createOrUpdate(Book book) {

        Author bookAuthor = book.getMy_author();
        Author savedAuthor = authorService.getOrCreate(bookAuthor);

        book.setMy_author(savedAuthor);
        bookRepository.save(book);
    }

    public Object find(String searchKey, String searchValue) throws Exception {

        switch (searchKey){
            case "id": {
                return cacheFind(searchValue);
            }
            case "genre":
                List<Book> booksByGenre = bookRepository.findByGenre(Genre.valueOf(searchValue));
                List<FindBookDTO> genreDtos = new ArrayList<>();
                for (Book book : booksByGenre) {
                    genreDtos.add(convertToDTO(book));
                }
                return genreDtos;
            case "author_name":
                List<Book> booksByAuthor = bookRepository.findByAuthor_Name(searchValue);
                List<FindBookDTO> authorDtos = new ArrayList<>();
                for (Book book : booksByAuthor) {
                    authorDtos.add(convertToDTO(book));
                }
                return authorDtos;
            case "name":
                return  convertToDTO(bookRepository.findByName(searchValue));
            default:
                throw new Exception("Search key not valid " + searchKey);
        }
    }

    public List <Book> findBy(Integer id){

        return bookRepository.findBookDataById(id);
    }
    public List<FindBookDTO> getAllBooks(){

         List<Book> books = bookRepository.findAll();
        List<FindBookDTO> convertDtos = new ArrayList<>();
        for (Book book : books) {
            convertDtos.add(convertToDTO(book));
        }
        return convertDtos;
    }

    public FindBookDTO cacheFind(String bookId) {

        Book book = bookcacheRepository.get(Integer.parseInt(bookId));
        if (book != null) {
            return convertToDTO(book);
        } else {
            book = bookRepository.findById(Integer.parseInt(bookId)).get();
            if (book != null) {
                bookcacheRepository.set(book);
            }
        } return convertToDTO(book);
    }
    private  FindBookDTO convertToDTO(Book book) {
        if (book == null) {
            return null;
        }
        FindBookDTO findBookDTO = new FindBookDTO();
        findBookDTO.setName(book.getName());
        findBookDTO.setAuthorName(book.getMy_author().getName());
        findBookDTO.setGenre(book.getGenre());

        return  findBookDTO;
    }
}