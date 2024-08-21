package com.ga5000.library.dtos.Transaction;

import com.ga5000.library.model.TransactionType;

import java.util.Date;


public record TransactionDTO(
        Long id,
        String bookTitle,
        String memberName,
        TransactionType transactionType,
        Date transactionDate,
        Date returnDate,
        boolean finished
) {}
