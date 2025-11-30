package com.paytabs.core.repository;

import com.paytabs.core.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByCardNumber(String cardNumber);
}
