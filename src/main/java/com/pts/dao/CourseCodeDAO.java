package com.pts.dao;

import com.pts.entity.CourseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCodeDAO extends JpaRepository<CourseCode,Integer> {
    @Query("select p from CourseCode p where p.code =?1")
    CourseCode laymacode(String code);

    @Query("SELECT c FROM CourseCode c WHERE c.account.tps_Username LIKE CONCAT('%', :keyword, '%')")
    List<CourseCode> timkiemTheoKhoaHoc(@Param("keyword") String keyword);

    @Query("select u from CourseCode u WHERE u.id =?1")
    CourseCode timcode(int id);
}
