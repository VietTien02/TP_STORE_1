package com.pts.dao;

import com.pts.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizDAO extends JpaRepository<Quiz,Integer> {
    @Query("SELECT p FROM Quiz p WHERE p.content.tps_id =?1")
    Quiz layquiz(int id);

    @Query("SELECT p FROM Quiz p WHERE p.id =?1")
    Quiz layquizid(int id);

    @Query("SELECT c FROM Quiz c WHERE c.course.tps_Name LIKE CONCAT('%', :keyword, '%')")
    List<Quiz> timkiemKhoaHoc(@Param("keyword") String keyword);


}
