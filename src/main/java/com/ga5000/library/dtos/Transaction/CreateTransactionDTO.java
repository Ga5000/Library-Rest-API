package com.ga5000.library.dtos.Transaction;

import com.ga5000.library.model.TransactionType;


public record CreateTransactionDTO(
        Long bookId,
        Long memberId,
        TransactionType transactionType
) {}
