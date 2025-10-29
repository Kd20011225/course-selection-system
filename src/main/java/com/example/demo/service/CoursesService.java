package com.example.demo.service;

import com.example.demo.dao.Course;

import java.util.List;

public interface CoursesService {

    Course getCourseByID(long courseID);
    List<Course> getAllCourses();
    void addCourse(Course course);
    void updateCourseSeats(long classId, int seatsOpen);
    void updateCourse(Course course);
    String selectCourse(long courseID, long studentID);
}
