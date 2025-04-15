package com.jpmc.midascore.component;

import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpmc.midascore.model.Transaction;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON deserialization

    @KafkaListener(topics = "midas-transactions", groupId = "midas-group")
    public void transactionListener(@Payload String message) {
        try {
            // Deserialize JSON message to Transaction object
            Transaction transaction = objectMapper.readValue(message, Transaction.class);

            // Log transaction details
            logger.info("🔥 Debug: Received transaction - Sender: {}, Recipient: {}, Amount: {}",
                    transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());
        } catch (Exception e) {
            logger.error("❌ Error processing Kafka message: {}", message, e);
        }
    }
}
