package com.ga5000.library.dtos.Transaction;

import com.ga5000.library.model.TransactionType;

import java.util.Date;

public record UpdateTransactionDTO(
        Long id,
        Long bookId,
        Long memberId,
        TransactionType transactionType,
        Date transactionDate
) {}
