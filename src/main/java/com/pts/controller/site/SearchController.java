package com.pts.controller.site;

import com.pts.dao.CourseDAO;
import com.pts.dto.Search;
import com.pts.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private CourseDAO courseDAO;

    @GetMapping("/search")
    public List<Search> search(@RequestParam("q") String query) {
        if (query.length() < 3) {
            throw new IllegalArgumentException("Search query must be at least 3 characters long.");
        }
        

        List<Course> courses = courseDAO.searchCourses(query);
        List<Search> results = new ArrayList<>();
        for (Course course : courses) {
            Search result = new Search();
            result.setName(course.getTps_Name());
            result.setImgae(course.getTps_image());
            result.setUrl("/khoa-hoc/" + course.getTps_id());
            results.add(result);
        }
        return results;
    }


}
