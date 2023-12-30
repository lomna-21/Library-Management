package com.example.LibraryManagement.DTO;


import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindBookDTO {

    private String name;

    private Genre genre;

    private String authorName;


    public Book to() {


        return Book.builder()
                .name(this.name)
                .genre(this.genre)
                .my_author(Author.builder().name(this.name).build())
                .build();
    }
}
