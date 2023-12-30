package com.example.LibraryManagement.DTO;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInfoDTO {


    private String name;

    private Genre genre;


    public Book to(){

        return Book.builder().name(this.name).genre(this.genre).build();
    }
}