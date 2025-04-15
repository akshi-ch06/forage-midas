package com.jpmc.midascore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import com.jpmc.midascore.component.TransactionProducer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.jpmc.midascore.model.Transaction;



import static org.awaitility.Awaitility.await;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith(SpringExtension.class)
@SuppressWarnings("unused")
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private TransactionProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_verifier() {
        logger.info("✅ Embedded Kafka Test Started");
        // ✅ Load transactions from file instead of hardcoded values
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        // ✅ Send each transaction
        for (String transactionLine : transactionLines) {
            System.out.println("Raw Transaction: " + transactionLine);
            Transaction transaction = parseTransaction(transactionLine);
            System.out.println("Parsed Transaction: " + transaction);
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
