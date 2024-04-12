package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "coursecode")
public class CourseCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course courseCode;

    String code;

    int quantity;
    @ManyToOne
    @JoinColumn(name = "username")
    private Account account;
    Boolean status;

//    @OneToMany(mappedBy = "courseCode")
//    List<Order> orders;
}
