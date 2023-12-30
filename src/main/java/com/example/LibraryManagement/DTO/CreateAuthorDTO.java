package com.example.LibraryManagement.DTO;


import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAuthorDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    private String bookName;

    private Genre genre;



    public Author to(){


        Author author = Author.builder().name(this.name).email(this.email).build();

        return author;
    }
}
