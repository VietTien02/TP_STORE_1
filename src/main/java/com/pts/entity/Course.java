package com.pts.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "course")
@Getter
@Setter
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tps_id;
    String tps_Name;
    String tps_image;
    int tps_discount;
    int tps_Price;

    String str_tps_discount;
    String str_tps_price;
    @Column(columnDefinition = "LONGTEXT")
    String tps_information;
    String learn;
    Date tps_date = new Date();
    boolean tps_Status;
    @ManyToOne
    @JoinColumn(name ="tps_categoryid")
    private Category category;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();
    @OneToMany(mappedBy = "courseor", fetch = FetchType.LAZY)
    List<Order> orders;

    @OneToMany(mappedBy = "courseCode")
    List<CourseCode> courseCodes;

    @OneToMany(mappedBy = "coursePayment")
    private Set<Payment> payments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    List<Quiz> quizzes;

    @OneToMany(mappedBy = "course")
    List<UserContent> userContents;

}
