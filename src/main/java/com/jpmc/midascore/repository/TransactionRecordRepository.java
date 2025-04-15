package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface TransactionRecordRepository extends CrudRepository<TransactionRecord, Long> {
    List<TransactionRecord> findBySenderId(long senderId);
    List<TransactionRecord> findByRecipientId(long recipientId);
}
