package com.pts.dao;

import com.pts.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizResultDAO extends JpaRepository<QuizResult,Long> {

    @Query("SELECT p FROM QuizResult p WHERE p.account.tps_Username =?1 and p.quiz.id = ?2")
    List<QuizResult> ktbai(String username, int id);

    @Query("SELECT p FROM QuizResult p WHERE p.account.tps_Username =?1 AND p.quiz.content.tps_id =?2 AND p.quiz.course.tps_id =?3")
    List<QuizResult> ketqua(String Username,int content,int course);
}

