package com.ga5000.library.controllers;

import com.ga5000.library.services.TransactionServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transaction")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionServiceImpl transactionService;

    public TransactionControllerImpl(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }
}
