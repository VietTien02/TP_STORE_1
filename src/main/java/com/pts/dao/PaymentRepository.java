package com.pts.dao;

import com.pts.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
//    public Payment getByTxnref(String txnRef);
    @Query("SELECT p FROM Payment p WHERE p.txnref=?1")
    Payment timma(String txnref);

    @Query("SELECT COUNT(o) FROM Payment o WHERE o.status='YES'")
    Long sumGD();

    @Query("SELECT c FROM Payment c WHERE c.username LIKE CONCAT('%', :keyword, '%')")
    List<Payment> timtaikhoan(@Param("keyword") String keyword);

    @Query("SELECT p FROM Payment p WHERE LOWER(p.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Payment> findPayments(@Param("keyword") String keyword, Pageable pageable);
}
