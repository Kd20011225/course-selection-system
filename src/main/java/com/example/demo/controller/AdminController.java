package com.example.demo.controller;

import com.example.demo.dao.Course;
import com.example.demo.service.CoursesService;
import com.example.demo.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private CoursesService coursesService;

    @Autowired
    private StudentsService studentsService;

    @PostMapping("/course/open/{courseID}")
    public String openClass(@PathVariable long courseID, @RequestBody Course course) {
        course.setCourseID(courseID);
        coursesService.addCourse(course);
        return "Course added successfully.";
    }

    @PostMapping("/student/updateStatus/{studentID}")
    public String updateStudentStatus(@PathVariable long studentID, @RequestParam double threshold) {
        double averageScore = studentsService.calculateAverageScore(studentID);
        int status = averageScore >= threshold ? 1 : 0;
        studentsService.updateStudentStatus(studentID, status);
        return "Student status updated successfully.";
    }

    @PostMapping("/course/update/{courseID}")
    public String updateCourse(@PathVariable long courseID, @RequestBody Course course) {
        course.setCourseID(courseID);
        coursesService.updateCourse(course);
        return "Course updated successfully.";
    }
}
