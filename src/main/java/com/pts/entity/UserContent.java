package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "usercontent")
    public class UserContent {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "course_id")
        private Course course;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_content")
        private Account account;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "content_id")
        private Content content;

        private boolean completed;

        String time;

}
