package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "favoriteCrouse")
public class FavoriteCrouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tps_id;
    String tps_Username;
    @ManyToOne
    @JoinColumn(name = "tps_CourseId")
    Course courseFavorite;
    Date tps_Date = new Date();

}
