package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.jpmc.midascore.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionProducer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(Transaction transaction) {
        try {
            String jsonTransaction = objectMapper.writeValueAsString(transaction); // Convert to JSON
            kafkaTemplate.send("midas-transactions", jsonTransaction);
        } catch (Exception e) {
            logger.error("🚨 Error serializing transaction", e);
        }
    }
}
