package com.pts.service;

import com.pts.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Course findById(int tps_id);

}
