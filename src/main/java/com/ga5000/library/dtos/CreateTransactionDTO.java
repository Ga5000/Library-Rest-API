package com.ga5000.library.dtos;

import com.ga5000.library.model.TransactionType;

import java.time.LocalDateTime;

public record CreateTransactionDTO(
        Long bookId,
        Long memberId,
        TransactionType transactionType,
        LocalDateTime transactionDate
) {}
