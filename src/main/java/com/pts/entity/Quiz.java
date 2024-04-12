package com.pts.entity;


import lombok.Data;

import javax.persistence.*;

import java.util.List;

@Data
@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
    Boolean status;
    @OneToMany(mappedBy = "quizQues", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> quizQuestionList;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizResult> quizResults;


}
