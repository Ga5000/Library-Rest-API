package com.ga5000.library.repositories;


import com.ga5000.library.model.Transaction;
import com.ga5000.library.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByMember_MemberId(Long memberId);
    List<Transaction> findByBook_BookId(Long bookId);
    List<Transaction> findByMember_MemberIdAndTransactionType(Long memberId, TransactionType transactionType);
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
