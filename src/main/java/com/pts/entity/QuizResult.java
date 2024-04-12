package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "quizresult")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuizQuestion question;

    private String user_answer;
  

    private boolean correct;

//    public boolean dapan(){
//        return this.question.getCorrectoption().equals(this.correct);
//
//    }


    private int score;


}
