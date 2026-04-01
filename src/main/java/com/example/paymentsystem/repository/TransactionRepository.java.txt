package com.example.paymentsystem.repository;

import com.example.paymentsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByReferenceId(String referenceId); 
    List<Transaction> findBySenderIdOrReceiverId(Long s, Long r);
}