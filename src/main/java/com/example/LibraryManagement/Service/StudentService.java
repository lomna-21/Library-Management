package com.example.LibraryManagement.Service;


import com.example.LibraryManagement.DAO.StudentCacheRepository;
import com.example.LibraryManagement.DAO.StudentRepository;
import com.example.LibraryManagement.DTO.StudentSelfDetailsDTO;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Security.SecuredUser;
import com.example.LibraryManagement.Security.SecurityService;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService  {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SecurityService service;

    @Autowired
    StudentCacheRepository cacheRepository;

    public void create(Student student) {
        SecuredUser securedUser = student.getSecuredUser();
        securedUser = service.save(securedUser, Constants.STUDENT_USER);

        student.setSecuredUser(securedUser);

        studentRepository.save(student);
    }

    public Student findStudent(Integer id){

        return studentRepository.findById(id).get();
    }
    public StudentSelfDetailsDTO find(Integer studentId) {
        Student student = cacheRepository.get(studentId);
        if(student != null){
            return convertToDTO(student);
        }
        else {
            student = studentRepository.findById(studentId).orElse(null);
            if (student != null) {
                cacheRepository.set(student);
            }
            return convertToDTO(student);
        }
    }

    private StudentSelfDetailsDTO convertToDTO(Student student) {
        if (student == null) {
            return null;
        }
        StudentSelfDetailsDTO studentSelfDetailsDTO = new StudentSelfDetailsDTO();
        studentSelfDetailsDTO.setAge(student.getAge());
        studentSelfDetailsDTO.setName(student.getName());
        studentSelfDetailsDTO.setEmail(student.getEmail());

        return  studentSelfDetailsDTO;
    }
}
