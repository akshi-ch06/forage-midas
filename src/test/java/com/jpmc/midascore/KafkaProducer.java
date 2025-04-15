package com.jpmc.midascore;

//import com.jpmc.midascore.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        try {
            // ✅ Split transaction data correctly
            String[] transactionData = transactionLine.split(",");

            if (transactionData.length != 3) {
                logger.error("❌ Invalid transaction format: {}", transactionLine);
                return;
            }

            // ✅ Parse transaction data
            long senderId = Long.parseLong(transactionData[0].trim());
            long recipientId = Long.parseLong(transactionData[1].trim());
            float amount = Float.parseFloat(transactionData[2].trim());

            // ✅ Construct a message
            String message = senderId + "," + recipientId + "," + amount;

            // ✅ Log before sending
            logger.info("📤 Sending transaction to Kafka: {}", message);

            kafkaTemplate.send(topic, message); // 🔄 Fix Type
        } catch (Exception e) {
            logger.error("🚨 Error sending transaction: {}", transactionLine, e);
        }
    }
}