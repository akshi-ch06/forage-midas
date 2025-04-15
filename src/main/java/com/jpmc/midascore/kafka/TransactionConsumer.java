package com.jpmc.midascore.kafka;

import com.jpmc.midascore.service.TransactionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
@SuppressWarnings("unused")
public class TransactionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);

    @Autowired
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON parser

    @KafkaListener(topics = "midas-transactions", groupId = "midas-core-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            // ✅ Deserialize JSON String to Transaction object
            Transaction transaction = objectMapper.readValue(record.value(), Transaction.class);

            logger.info("Received message from Kafka: {}", transaction);

            // ✅ Process the transaction
            boolean success = transactionService.processTransaction(
                    transaction.getSenderId(),
                    transaction.getRecipientId(),
                    transaction.getAmount()
            );

            if (success) {
                logger.info("✅ Transaction processed successfully: {}", transaction);
            } else {
                logger.warn("❌ Transaction failed: {}", transaction);
            }
        } catch (Exception e) {
            logger.error("🚨 Error processing transaction: {}", record.value(), e);
        }
    }
}
