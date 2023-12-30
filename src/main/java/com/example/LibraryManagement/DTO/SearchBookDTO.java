package com.example.LibraryManagement.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookDTO {

    @NotBlank
    private String searchKey;

    @NotBlank
    private String searchValue;
}