package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.component.TransactionProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@SuppressWarnings("unused")
public class MidasCoreApplication {
    // ✅ Define Logger
    private static final Logger logger = LoggerFactory.getLogger(MidasCoreApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    // ✅ Use @Bean to ensure proper initialization of Kafka Producer
    @Bean
    CommandLineRunner sendTestTransaction(TransactionProducer producer) {
        return args -> {
            try {
                Transaction transaction = new Transaction(1L, 67890L, 100.0f);
                producer.sendTransaction(transaction);
                logger.info("✅ Test transaction sent: {}", transaction);
            } catch (Exception e) {
                logger.error("❌ Error occurred while sending test transaction", e);
            }
        };
    }
}
