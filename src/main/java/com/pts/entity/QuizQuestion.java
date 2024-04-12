package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "quizquestion")
    public class QuizQuestion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String question;

        private String optionA;

        private String optionB;

        private String optionC;

        private String optionD;

        private String correctoption;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "quiz_id")
        private Quiz quizQues;

        @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<QuizResult> quizResults;
    }
