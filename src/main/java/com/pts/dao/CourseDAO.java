package com.pts.dao;

import com.pts.entity.Course;
import com.pts.entity.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseDAO extends JpaRepository<Course,String> {
    //lay tat ca khoa hoc
    List<Course> findAll();

    //lay khoa hoc theo id (Trang chi tiet)
    @Query("SELECT p FROM Course p WHERE p.tps_id=?1")
    Course findById(int tps_id);
    @Query("SELECT c FROM Course c WHERE c.tps_Status = true AND LOWER(c.tps_Name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);
    @Query("SELECT p FROM Course p WHERE p.category.tps_Id=?1")
    List<Course>laydanhmuc(int id);
    @Query("SELECT c from Course  c WHERE c.tps_Name = :name")
    Course findByName(String name);
    @Query("SELECT p FROM Course p WHERE p.tps_id=?1")
    Course findByIdne(UserContent userContent);

    @Query("SELECT c.tps_id, c.tps_Name,c.tps_Price, COUNT(o.tps_id) as sold "
            + "FROM Course c LEFT JOIN c.orders o ON c.tps_id = o.courseor.tps_id  "
            + "GROUP BY c.tps_id, c.tps_Name,c.tps_Price")
    List<Object[]> findAllCoursesAndSoldCount();
}
