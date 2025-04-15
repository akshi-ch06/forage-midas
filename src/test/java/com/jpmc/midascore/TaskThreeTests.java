package com.jpmc.midascore;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
@SuppressWarnings("unused")
public class TaskThreeTests {

    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository; // ✅ Added for verification

    @Test
    void task_three_verifier(){
        // ✅ Populate test users
        userPopulator.populate();

        // ✅ Load transaction test data (replace with correct file)
        String[] transactionLines = fileLoader.loadStrings("/test_data/test_transactions.txt");

        // ✅ Send transactions to Kafka
        for (String transactionLine : transactionLines) {
            logger.info("📤 Sending transaction: {}", transactionLine);
            kafkaProducer.send(transactionLine);
        }

        // ✅ Wait for processing instead of Thread.sleep()
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            User waldorf = userRepository.findById(1L).orElse(null);
            return waldorf != null && waldorf.getBalance() >= 200;
        });

        // ✅ Verify results
        User waldorf = userRepository.findById(1L).orElse(null);
        assertNotNull(waldorf, "User 'waldorf' not found in database");

        logger.info("✅ Waldorf's final balance: {}", waldorf.getBalance());

        // ✅ Use a precision margin to avoid rounding issues
        assertEquals(200.0, waldorf.getBalance(), 0.01, "❌ Balance mismatch! Check transaction processing.");
    }
}
