package com.example.LibraryManagement.Controller;


import com.example.LibraryManagement.DTO.*;
import com.example.LibraryManagement.Security.SecuredUser;
import com.example.LibraryManagement.Service.*;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    AdminService adminService;

    @Autowired
    AuthorService authorService;

    @Autowired
    BookService bookService;

    @Autowired
    StudentService studentService;

    @Autowired
    private TransactionService transactionService;


/*--------------------------------------------------------------------------------------------------------------------*/

    //ADMIN APIs
    @PostMapping("/admin")
    public void createAdmin(@RequestBody(required = true) @Valid AdminDTO createAdminRequest) throws Exception {

        adminService.create(createAdminRequest.to());
    }

    @GetMapping("/student-by-id/")
    public StudentSelfDetailsDTO findStudent(@RequestParam(value = "id") Integer studentId) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();

        for(GrantedAuthority grantedAuthority: securedUser.getAuthorities()){
            String[] authorities = grantedAuthority.getAuthority().split(Constants.DELIMITER);
            boolean isCalledByAdmin = Arrays.stream(authorities).anyMatch(x -> Constants.STUDENT_INFO_AUTHORITY.equals(x));
            if (isCalledByAdmin) {
                return studentService.find(studentId);
            }
        }throw new Exception("User is not authorized to do this");
    }

    @PostMapping("/author")
    public void createAuthor(@RequestBody(required = true) @Valid CreateAuthorDTO authorRequest) throws Exception {

        authorService.getOrCreate(authorRequest.to());
    }

    @GetMapping("/authors")
    public List<CreateAuthorDTO> authorList(){
        return authorService.getAllAuthors();
    }

    @PostMapping("/book")
    public void createBook(@RequestBody(required = true) @Valid BookDTO createBookRequest) throws Exception {

        bookService.createOrUpdate(createBookRequest.to());
    }

    @PostMapping("/transaction")
    public String initiateTxn(@RequestBody(required = true) @Valid TransactionDTO transactionRequest) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        Integer adminId = securedUser.getAdmin().getId();
        return transactionService.initiateTxn(transactionRequest ,adminId);
    }

    /*----------------------------------------------STUDENT APIs------------------------------------------------------*/

    @PostMapping("/student/signup")
    public void createStudent(@RequestBody(required = true) @Valid StudentDTO studentRequest) {

        studentService.create(studentRequest.to());
    }


    @GetMapping("/student")
    public StudentSelfDetailsDTO findStudent(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        Integer studentId = securedUser.getStudent().getId();

        return studentService.find(studentId);
    }


    /*-------------------------------------------Accessible By All----------------------------------------------------*/


    @GetMapping("/findBook")
    public Object getBooks(@RequestBody(required = true) @Valid SearchBookDTO searchBookRequest) throws Exception {

        return  bookService.find(
                searchBookRequest.getSearchKey(),
                searchBookRequest.getSearchValue());
    }

    @GetMapping("/books")
    public List<FindBookDTO> allBooks(){
        return bookService.getAllBooks();
    }


    @PostMapping("/payment")
    public void makePayment(@RequestParam("amount") Integer amount,
                            @RequestParam("transactionId") String txnId) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        Integer studentId =securedUser.getStudent().getId();
        transactionService.payFine(amount ,studentId ,txnId);
    }

}
