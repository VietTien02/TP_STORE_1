package com.pts.dao;

import com.pts.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizQuestionDAO extends JpaRepository<QuizQuestion,Long> {
    @Query("SELECT p FROM QuizQuestion p WHERE p.quizQues.content.tps_id =?1")
    List<QuizQuestion> findQuizQuestion(int contentId);

    @Query("SELECT p FROM QuizQuestion  p WHERE p.id =?1")
    QuizQuestion layid(long id);
    @Query("SELECT p FROM QuizQuestion p WHERE p.quizQues.id =?1")
    List<QuizQuestion>timidquiz(int id);
}
