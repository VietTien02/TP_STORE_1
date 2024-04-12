package com.pts.dao;

import com.pts.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
    @Query("SELECT c FROM Category c WHERE c.tps_Name LIKE CONCAT('%', :keyword, '%')")
    List<Category> timkiem(@Param("keyword") String keyword);
}
