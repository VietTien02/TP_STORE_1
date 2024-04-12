package com.pts.service.impl;

import com.pts.dao.CourseDAO;
import com.pts.service.CourseService;
import com.pts.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseDAO cdao;


    @Override
    public List<Course> findAll() {
        return cdao.findAll();
    }

    @Override
    public Course findById(int tps_id) {
        return cdao.findById(tps_id);
    }




}
