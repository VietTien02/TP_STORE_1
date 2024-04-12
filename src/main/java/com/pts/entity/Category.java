package com.pts.entity;


import lombok.Data;
import javax.persistence.OneToMany;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "category")
public class Category implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tps_Id;
    String tps_Name;
    String tps_Image;

    @OneToMany(mappedBy = "category")
    List<Course> courses;


}
