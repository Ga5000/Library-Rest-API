package com.ga5000.library.repositories;

import com.ga5000.library.dtos.TransactionDTO;
import com.ga5000.library.model.Transaction;
import com.ga5000.library.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByMemberId(Long memberId);
    List<Transaction> findByBookId(Long bookId);
    List<Transaction> findByMemberIdAndTransactionType(Long memberId, TransactionType transactionType);
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
