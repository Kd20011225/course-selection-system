package com.example.demo.controller;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.dao.Course;
import com.example.demo.service.CoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CoursesService coursesService;

    @GetMapping("/check/{courseID}")
    public Course checkClass(@PathVariable long courseID) {
        try {
            Course course = coursesService.getCourseByID(courseID);
            logger.info("Retrieved course with ID {}.", courseID);
            return course;
        } catch (UserException e) {
            logger.error("Error retrieving course with ID {}: {}", courseID, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving course with ID {}: {}", courseID, e.getMessage());
            throw new UserException("Error retrieving course.");
        }
    }

    @GetMapping("/all")
    public List<Course> getAllCourses() {
        try {
            List<Course> courses = coursesService.getAllCourses();
            logger.info("Retrieved all courses. Total count: {}", courses.size());
            return courses;
        } catch (Exception e) {
            logger.error("Error retrieving all courses: {}", e.getMessage());
            throw new UserException("Error retrieving courses.");
        }
    }

    @PostMapping("select/{courseID}")
    public String selectCourse(@PathVariable long courseID, @RequestParam long studentID) {
        try {
            String result = coursesService.selectCourse(courseID, studentID);
            logger.info("Student with ID {} enrolled in course ID {} successfully.", studentID, courseID);
            return result;
        } catch (UserException e) {
            logger.error("Error enrolling student ID {} in course ID {}: {}", studentID, courseID, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error enrolling student ID {} in course ID {}: {}", studentID, courseID, e.getMessage());
            return "Error enrolling in course.";
        }
    }

}