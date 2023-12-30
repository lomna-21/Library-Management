package com.example.LibraryManagement.DAO;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Model.Transaction;
import com.example.LibraryManagement.Model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {


Transaction findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(Student student, Book book, TransactionType transactionType);

    Transaction findByTxnId(String txnId);

}