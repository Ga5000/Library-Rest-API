package com.ga5000.library.services;

import com.ga5000.library.dtos.Transaction.CreateTransactionDTO;
import com.ga5000.library.dtos.Transaction.RenewTransactionDTO;
import com.ga5000.library.dtos.Transaction.ReturnTransactionDTO;
import com.ga5000.library.dtos.Transaction.TransactionDTO;
import com.ga5000.library.model.Book;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(CreateTransactionDTO createTransactionDTO);


    TransactionDTO getTransactionById(Long id);


    List<TransactionDTO> getTransactionsByMemberId(Long memberId);


    List<TransactionDTO> getTransactionsByBookId(Long bookId);


    List<TransactionDTO> getAllTransactions();


    TransactionDTO renew(Long id, RenewTransactionDTO updateTransactionDTO);
    TransactionDTO returnBook(Long id, ReturnTransactionDTO returnTransactionDTO);


    void deleteTransaction(Long id);

    boolean isValidTransaction(Long id);

    List<Book> getCurrentBorrowedBooks(Long memberId);


    List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
