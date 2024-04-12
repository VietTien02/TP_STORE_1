package com.pts.dao;


import com.pts.entity.Account;
import com.pts.entity.Authority;
import com.pts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityDao extends JpaRepository<Authority,Integer> {
    @Query("SELECT p.role FROM Authority p WHERE p.account.tps_Username=?1")
    List<Role>findRolesByUsername(String username);
    @Query("SELECT DISTINCT p FROM Authority p WHERE p.account IN ?1")
    List<Authority>findAuthoritiesOfAdministrators(List<Account>accounts);
    @Query("SELECT p FROM Authority  p WHERE p.account.tps_Username =?1")
    Authority laytk(String username);

    @Query("SELECT c FROM Authority c WHERE  c.account.tps_Username like  '%'+:keyword+'%' ")
    List<Authority> timkiem(@Param("keyword") String keyword);

}
