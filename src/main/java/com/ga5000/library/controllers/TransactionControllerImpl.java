package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Transaction.CreateTransactionDTO;
import com.ga5000.library.dtos.Transaction.RenewTransactionDTO;
import com.ga5000.library.dtos.Transaction.ReturnTransactionDTO;
import com.ga5000.library.dtos.Transaction.TransactionDTO;
import com.ga5000.library.services.TransactionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionServiceImpl transactionService;

    public TransactionControllerImpl(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Override
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody @Valid CreateTransactionDTO createTransactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body( transactionService.createTransaction(createTransactionDTO));
    }

    @GetMapping("/{transactionId}")
    @Override
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable("transactionId") Long transactionId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/member/{memberId}")
    @Override
    public ResponseEntity<List<TransactionDTO>> getMemberTransactions(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsByMemberId(memberId));
    }

    @GetMapping("/book/{bookId}")
    @Override
    public ResponseEntity<List<TransactionDTO>> getBookTransactions(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsByBookId(bookId));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllTransactions());
    }

    @PutMapping("/renew/{transactionId}")
    @Override
    public ResponseEntity<TransactionDTO> renew(@PathVariable("transactionId") Long transactionId,
                                                @RequestBody @Valid RenewTransactionDTO renewTransactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.renew(transactionId,renewTransactionDTO));
    }

    @PutMapping("/return/{transactionId}")
    @Override
    public ResponseEntity<TransactionDTO> returnBook(@PathVariable("transactionId") Long transactionId,
                                                     @RequestBody @Valid ReturnTransactionDTO renewTransactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.returnBook(transactionId,renewTransactionDTO));
    }

    @DeleteMapping("/{transactionId}")
    @Override
    public ResponseEntity<Void> deleteTransaction(@PathVariable("transactionId") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
