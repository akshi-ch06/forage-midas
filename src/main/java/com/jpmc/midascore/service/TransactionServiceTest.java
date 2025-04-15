package com.jpmc.midascore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);

    @Mock // ✅ Mock the repository to prevent actual DB access
    private UserRepository userRepository;

    @Mock
    private TransactionRecordRepository transactionRecordRepository;

    @InjectMocks // ✅ Inject mocks into TransactionService
    private TransactionService transactionService;

    private User sender;
    private User recipient;

    @BeforeEach
    void setUp() {
        sender = new User("Alice", 100.0f);
        sender.setId(1L);

        recipient = new User("Bob", 50.0f);
        recipient.setId(2L);
    }

    @Test
    public void testBalanceAfterTransaction() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));

        float senderBefore = sender.getBalance();
        float recipientBefore = recipient.getBalance();

        boolean success = transactionService.processTransaction(sender.getId(), recipient.getId(), 50.0f);
        assertTrue(success, "❌ Transaction failed unexpectedly!");

        // 🔥 No need to fetch from userRepository again, use updated objects directly
        float expectedSenderBalance = senderBefore - 50.0f;
        float expectedRecipientBalance = recipientBefore + 50.0f;

        assertEquals(expectedSenderBalance, sender.getBalance(), 0.01, "❌ Sender Balance mismatch!");
        assertEquals(expectedRecipientBalance, recipient.getBalance(), 0.01, "❌ Recipient Balance mismatch!");
    }
}
