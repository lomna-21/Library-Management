package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DAO.TransactionRepository;
import com.example.LibraryManagement.DTO.TransactionDTO;
import com.example.LibraryManagement.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    AdminService adminService;

    @Autowired
    BookService bookService;

    @Value("${student.allowed.max-books}") // from application.properties
    Integer maxBooksAllowed;

    @Value("${student.allowed.duration}")
    Integer duration;

    public String initiateTxn(TransactionDTO request, Integer adminId) throws Exception {

        return request.getTransactionType() == TransactionType.ISSUE
                ? issuance(request, adminId) :
                returnBook(request, adminId);
    }

    private String issuance(TransactionDTO request, Integer adminId) throws Exception {
        Student student = studentService.findStudent(request.getStudentId());
        Admin admin = adminService.find(adminId);
        List<Book> bookList =  bookService.findBy(request.getBookId());

        if (student == null
                || admin == null
                || bookList.isEmpty()
                || bookList.get(0).getStudent() != null
                || student.getBookList().size() >= maxBooksAllowed) {
            throw new Exception("Invalid request");
        }

        Transaction transaction = Transaction.builder()
                .txnId(UUID.randomUUID().toString())
                .student(student)
                .book(bookList.get(0))
                .admin(admin)
                .transactionType(request.getTransactionType())
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        try {
            transactionRepository.save(transaction);

            Book book = bookList.get(0);
            book.setStudent(student);
            bookService.createOrUpdate(book);

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        } finally {
            transactionRepository.save(transaction);
        }

        return transaction.getTxnId();
    }


    private String returnBook(TransactionDTO request, Integer adminId) throws Exception {
        /**
         * Return
         * 1. If the book is valid or not and student is valid or not
         * 2. entry in the Txn table
         * 3. due date check and fine calculation
         * 4. if there is no fine, then de-allocate the book from student's name ==> book table
         */

        Student student = studentService.findStudent(request.getStudentId());
        Admin admin = adminService.find(adminId);
        List<Book> bookList = bookService.findBy(request.getBookId());

        Book book = bookList != null && bookList.size() > 0 ? bookList.get(0) : null;

        if (student == null
                || admin == null // admin is null
                || book == null
                || book.getStudent() == null  // if the book is assigned to someone or not
                || !book.getStudent().getId().equals(student.getId())) { // if the book is assigned to the same student
            // which is requesting to return or not
            throw new Exception("Invalid request");
        }

        // Getting the corresponding issuance txn
        Transaction issuanceTxn = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(
                student, book, TransactionType.ISSUE);
        if(issuanceTxn == null){
            throw new Exception("Invalid request");
        }

        Transaction transaction = null;
        try {
            Integer fine = calculateFine(issuanceTxn.getCreatedOn());
            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .transactionType(request.getTransactionType())
                    .transactionStatus(TransactionStatus.PENDING)
                    .fine(fine)
                    .build();

            transactionRepository.save(transaction);

            if (fine == 0) {
                book.setStudent(null);
                bookService.createOrUpdate(book);
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            }
        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }finally {
            transactionRepository.save(transaction);
        }

        return transaction.getTxnId();
    }

    // S1 --> B1 = D1
    // S1 --> B1 = D2

    private Integer calculateFine(Date issuanceTime){

        long issueTimeInMillis = issuanceTime.getTime();
        long currentTime = System.currentTimeMillis();

        long diff = currentTime - issueTimeInMillis;

        long daysPassed = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if(daysPassed > duration){
            return (int)(daysPassed - duration);
        }

        return 0;
    }

    public void payFine(Integer amount, Integer studentId, String txnId) throws Exception {

        Transaction returnTxn = transactionRepository.findByTxnId(txnId);

        Book book = returnTxn.getBook();

        if(returnTxn.getFine() == amount && book.getStudent() != null && book.getStudent().getId() == studentId){
            returnTxn.setTransactionStatus(TransactionStatus.SUCCESS);
            book.setStudent(null);
            bookService.createOrUpdate(book);
            transactionRepository.save(returnTxn);
        }else{
            throw new Exception("invalid request");
        }

    }
}