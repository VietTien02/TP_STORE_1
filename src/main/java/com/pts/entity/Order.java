package com.pts.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "neworder")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tps_id;

    String tps_username;
    @ManyToOne
    @JoinColumn(name = "tps_courseid")
    Course courseor;

    @Temporal(TemporalType.DATE)
    Date tps_date = new Date();

//    @ManyToOne
//    @JoinColumn(name = "tps_code")
//    private CourseCode courseCode;
//    String tps_code;

    Boolean tps_statuscode;
    String pay;
    @Transient
    private int courseProgress;


}
