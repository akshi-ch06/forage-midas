package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User getUserWithTransactions(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Hibernate.initialize(user.getSentTransactions());
        Hibernate.initialize(user.getReceivedTransactions());
        return user;
    }
}
