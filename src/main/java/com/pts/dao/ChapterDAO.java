package com.pts.dao;

import com.pts.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterDAO extends JpaRepository<Chapter,String> {
    @Query("SELECT p FROM Chapter p WHERE p.course.tps_id = ?1")
    List<Chapter>findByCourse(int id);

    @Query("SELECT p FROM Chapter p WHERE p.tps_id = ?1")
    Chapter findcourse(int id);
    @Query("SELECT p FROM Chapter p WHERE p.tps_id = ?1")
    Chapter find(int id);

    @Query("SELECT c FROM Chapter c WHERE c.tps_title LIKE CONCAT('%', :keyword, '%')")
    List<Chapter> timkiem(@Param("keyword") String keyword);

    @Query("SELECT c FROM Chapter c WHERE c.course.tps_Name LIKE CONCAT('%', :keyword, '%')")
    List<Chapter> timkiemKhoaHoc(@Param("keyword") String keyword);

    @Query("SELECT c FROM Chapter c WHERE c.tps_title = ?1 AND c.course.tps_id = ?2")
    Chapter timKiemChuongTheoNoiDungVaTheoId(String name, int courseId);





}
