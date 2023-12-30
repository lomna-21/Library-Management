package com.example.LibraryManagement.DTO;

import com.example.LibraryManagement.Model.Student;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSelfDetailsDTO {


    private String name;

    private String email;

    private Integer age;


    public Student to(){

        return Student.builder().name(this.name).email(this.email).age(this.age).build();
    }
}
