package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Transaction.CreateTransactionDTO;
import com.ga5000.library.dtos.Transaction.RenewTransactionDTO;
import com.ga5000.library.dtos.Transaction.ReturnTransactionDTO;
import com.ga5000.library.dtos.Transaction.TransactionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionController {
    ResponseEntity<TransactionDTO> createTransaction(CreateTransactionDTO createTransactionDTO);
    ResponseEntity<TransactionDTO> getTransactionById(Long transactionId);
    ResponseEntity<List<TransactionDTO>> getMemberTransactions(Long memberId);
    ResponseEntity<List<TransactionDTO>> getBookTransactions(Long bookId);
    ResponseEntity<List<TransactionDTO>> getAllTransactions();
    ResponseEntity<TransactionDTO> renew(Long transactionId, RenewTransactionDTO renewTransactionDTO);
    ResponseEntity<TransactionDTO> returnBook(Long transactionId, ReturnTransactionDTO returnTransactionDTO);
    ResponseEntity<Void> deleteTransaction(Long transactionId);
}
