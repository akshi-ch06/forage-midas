package com.jpmc.midascore.controller;

import com.jpmc.midascore.component.TransactionProducer;
import org.springframework.web.bind.annotation.*;
import com.jpmc.midascore.model.Transaction;


@RestController
@SuppressWarnings("unused")
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionProducer transactionProducer;

    public TransactionController(TransactionProducer transactionProducer) {
        this.transactionProducer = transactionProducer;
    }

    @PostMapping("/send")
    public String sendTransaction(@RequestBody Transaction transaction) {
        transactionProducer.sendTransaction(transaction);
        return "✅ Transaction sent to Kafka: " + transaction;
    }
}
