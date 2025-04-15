package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@SuppressWarnings("unused")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private float balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionRecord> sentTransactions;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionRecord> receivedTransactions;

    // ✅ Default Constructor
    public User() {}

    // ✅ Parameterized Constructor
    public User(String username, float balance) { // Remove ID from constructor, let it auto-generate
        this.username = username;
        this.balance = balance;
    }

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }

    public List<TransactionRecord> getSentTransactions() { return sentTransactions; }
    public void setSentTransactions(List<TransactionRecord> sentTransactions) { this.sentTransactions = sentTransactions; }

    public List<TransactionRecord> getReceivedTransactions() { return receivedTransactions; }
    public void setReceivedTransactions(List<TransactionRecord> receivedTransactions) { this.receivedTransactions = receivedTransactions; }
}
