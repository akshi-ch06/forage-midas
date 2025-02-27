package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.jpmc.midascore.component.TransactionProducer;

@EnableKafka
@SpringBootApplication
public class MidasCoreApplication {
    // ✅ Define Logger
    private static final Logger logger = LoggerFactory.getLogger(MidasCoreApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MidasCoreApplication.class, args);
        MidasCoreApplication app = context.getBean(MidasCoreApplication.class);

        try {
            app.sendTestTransaction(context).run(); // ✅ Manually call the method
        } catch (Exception e) {
            logger.error("Error occurred while sending test transaction", e); // ✅ Proper logging
        }
    }

    @Autowired
    CommandLineRunner sendTestTransaction(ApplicationContext context) {
        return args -> {
            Transaction transaction = new Transaction(1L, 67890L, 100.0f);
            TransactionProducer producer = context.getBean(TransactionProducer.class);
            producer.sendTransaction(transaction);
        };
    }
}