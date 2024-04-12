package com.pts.dao;

import com.pts.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Integer> {
    TransactionEntity findByTransactionno(String transactionNo);
}
