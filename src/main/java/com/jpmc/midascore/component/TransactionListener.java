package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    public TransactionListener() {
        logger.info("🚀 TransactionListener Initialized!");
    }

    @KafkaListener(topics = "midas-transactions", groupId = "midas-group")
    public void listen(ConsumerRecord<String, Transaction> record) {
        logger.info("✅ Received transaction: {}", record.value());
    }
}
