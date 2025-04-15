package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(@NonNull Long id);

    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    // ✅ Fetch user with transactions using JOIN FETCH to avoid LazyInitializationException
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.sentTransactions LEFT JOIN FETCH u.receivedTransactions WHERE u.id = :userId")
    Optional<User> findUserWithTransactions(@NonNull Long userId);
}
