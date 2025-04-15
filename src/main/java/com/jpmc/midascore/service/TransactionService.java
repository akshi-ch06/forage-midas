package com.jpmc.midascore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized boolean processTransaction(Long senderId, Long recipientId, BigDecimal amount) {
        logger.info("🔄 Processing transaction: sender={}, recipient={}, amount={}", senderId, recipientId, amount);

        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> recipientOpt = userRepository.findById(recipientId);

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            logger.warn("❌ Transaction failed: sender or recipient not found.");
            return false;
        }

        logger.info("✅ Fetching initial balances...");
        User sender = senderOpt.get();
        User recipient = recipientOpt.get();

        logger.info("📊 Initial Balances → Sender: {}, Recipient: {}", sender.getBalance(), recipient.getBalance());

        // ✅ Validate transaction
        if (sender.getBalance().compareTo(amount) < 0) {
            logger.warn("❌ Transaction failed: Insufficient funds.");
            return false;
        }

        logger.info("💳 Deducting {} from sender {}", amount, sender.getId());
        logger.info("💰 Adding {} to recipient {}", amount, recipient.getId());
        // ✅ Update balances
        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        // ✅ Save updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Ensure balances are updated in DB
        userRepository.flush();

        // ✅ Save transaction record
        logger.info("📝 Preparing to save transaction: sender={}, recipient={}, amount={}", sender.getId(), recipient.getId(), amount);
        TransactionRecord transaction = new TransactionRecord(sender, recipient, amount);
        transactionRecordRepository.save(transaction);
        logger.info("✅ Transaction saved successfully: {}", transaction);

        return true; //Success
    }
}
