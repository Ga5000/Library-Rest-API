package com.ga5000.library.dtos;

import com.ga5000.library.model.TransactionType;

public record ReturnTransactionDTO(Long bookId,
                                   Long memberId,
                                   TransactionType transactionType) {
}
