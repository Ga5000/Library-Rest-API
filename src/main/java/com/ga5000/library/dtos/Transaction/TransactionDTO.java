package com.ga5000.library.dtos.Transaction;

import com.ga5000.library.model.TransactionType;

import java.time.LocalDateTime;


public record TransactionDTO(
        Long id,
        String bookTitle,
        String memberName,
        TransactionType transactionType,
        LocalDateTime transactionDate,
        LocalDateTime returnDate,
        boolean finished
) {}
