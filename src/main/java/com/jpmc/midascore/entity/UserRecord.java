package com.jpmc.midascore.entity;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@SuppressWarnings("unused")
@Table(name = "user_record") // Add table name to avoid conflicts
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure proper ID generation
    private Long id;

    @Column(nullable = false, unique = true) // Ensure uniqueness like in User
    private String username;

    @Column(nullable = false)
    private BigDecimal balance;

    // ✅ Default Constructor
    public UserRecord() {}

    // ✅ Parameterized Constructor
    public UserRecord(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getUsername() { return username; } // Renamed correctly
    public BigDecimal getBalance() { return balance; }

    // ✅ Setter
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    // ✅ Proper toString() format
    @Override
    public String toString() {
        return String.format("UserRecord[id=%d, username='%s', balance=%.2f]", id, username, balance);
    }
}
