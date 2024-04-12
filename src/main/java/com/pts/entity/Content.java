package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "content")
public class Content implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        int tps_id;
        String tps_title;
        String tps_linkytb;
        @ManyToOne
        @JoinColumn(name = "tps_idchapter")
        private Chapter chapter;

        @OneToMany(mappedBy = "content")
        List<UserContent> userContents;
        @OneToMany(mappedBy = "content")
        List<Quiz> quizzes;
    }


