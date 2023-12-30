package com.example.LibraryManagement.DTO;

import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    @NotBlank
    private String name;

    @NotNull
    private Genre genre;

    @NotBlank
    private String authorName;

    @NotBlank
    private String authorEmail;

//    @NotNull
//    private Integer adminId;

    public Book to() {

        Author author = Author.builder()
                .name(this.authorName)
                .email(this.authorEmail)
                .build();

        return Book.builder()
                .name(this.name)
                .genre(this.genre)
                .my_author(author)
//                .my_admin(adminId)
                .build();
    }
}