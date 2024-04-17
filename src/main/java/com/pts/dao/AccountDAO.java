package com.pts.dao;

import com.pts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountDAO extends JpaRepository<Account,String> {

    @Query("SELECT p FROM Account p WHERE p.tps_Username = :username")
    Account laytk(String username);
//    @Query("select p from Account p where p.tps_Gmail =?1")
//    Account layemail(String username);
    @Query("SELECT p FROM Account  p WHERE p.activationToken=?1")
    Account findByActivationToken(String token);

    @Query("SELECT p FROM Account p WHERE p.resetToken=?1")
    Account laytokenreset(String token);

    @Query("SELECT a FROM Account a WHERE (:keyword IS NULL OR :keyword = '') OR a.tps_Username LIKE CONCAT('%', :keyword, '%')")
    List<Account> timkiemacc(@Param("keyword") String keyword);



}
