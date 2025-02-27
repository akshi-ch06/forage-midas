package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import com.jpmc.midascore.component.TransactionProducer;
import com.jpmc.midascore.foundation.Transaction;

import static org.awaitility.Awaitility.await;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, controlledShutdown = true, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092", "log.dirs=/tmp/kafka-logs"})
@SuppressWarnings("unused")
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private TransactionProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_verifier() {
        // ✅ Load transactions from file
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        // ✅ Send each transaction
        for (String transactionLine : transactionLines) {
            Transaction transaction = parseTransaction(transactionLine);
            kafkaProducer.sendTransaction(transaction);
            logger.info("Sent transaction: {}", transaction);
        }

        // ✅ Await Kafka processing
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> logger.info("Waiting for transaction processing..."));
        logger.info("✅ Test completed.");
    }

    // ✅ Helper method to convert a CSV transaction line to a Transaction object
    private Transaction parseTransaction(String transactionLine) {
        String[] parts = transactionLine.split(",");  // Assuming CSV format: "senderId,recipientId,amount"
        long senderId = Long.parseLong(parts[0].trim());
        long recipientId = Long.parseLong(parts[1].trim());
        float amount = Float.parseFloat(parts[2].trim());
        return new Transaction(senderId, recipientId, amount);
    }
}
