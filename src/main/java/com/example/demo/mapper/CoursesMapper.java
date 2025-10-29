package com.example.demo.mapper;

import com.example.demo.dao.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CoursesMapper {
    @Select("SELECT * FROM courses WHERE courseID = #{courseID}")
    Course findByCourseID(@Param("courseID") long courseID);

    @Select("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Insert("INSERT INTO courses (courseID, courseName, professorID, professorName, openSeason, seatsOpen, durationPeriod, prereqCourse, requireMajor) " +
            "VALUES (#{courseID}, #{courseName}, #{professorID}, #{professorName}, #{openSeason}, #{seatsOpen}, #{durationPeriod}, #{prereqCourse}, #{requireMajor})")
    void insert(Course course);

    @Update("UPDATE courses SET seatsOpen = #{seatsOpen} WHERE courseID = #{courseID}")
    void updateSeats(@Param("courseID") long courseID, @Param("seatsOpen") int seatsOpen);

    @Update("UPDATE courses SET courseName = #{courseName}, professorID = #{professorID}, professorName = #{professorName}, " +
            "openSeason = #{openSeason}, seatsOpen = #{seatsOpen}, durationPeriod = #{durationPeriod}, prereqCourse = #{prereqCourse}, " +
            "requireMajor = #{requireMajor} WHERE courseID = #{courseID}")
    void updateCourse(Course course);

    @Select("SELECT * FROM courses WHERE prereqCourse IS NOT NULL AND prereqCourse != ''")
    List<Course> findAllCoursesWithPrerequisites();
}
