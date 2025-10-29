package com.example.demo.controller;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.dao.Student;
import com.example.demo.service.StudentHistoryService;
import com.example.demo.service.StudentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentsService studentsService;
    private StudentHistoryService studentHistoryService;

    @GetMapping("/{studentID}")
    public Student getStudentByID(@PathVariable long studentID) {

        Student student = studentsService.getStudentByID(studentID);
        if (Objects.isNull(student) || Objects.isNull(student.getStudentID())) {
            logger.error("studentID can not found {}", studentID);
            throw new UserException("studentID can not found: " + studentID);
        }
        logger.info("Successfully find user by studentID {}", studentID);
        return student;
    }

    @PostMapping("/update/{studentID}")
    public String updateStudent(@PathVariable long studentID, @RequestBody Student student) {
        try {

            if (studentID != student.getStudentID()) {
                throw new UserException("Student ID in path and body do not match.");
            }
            studentsService.updateStudent(student);
            return "Student information updated successfully.";
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error updating student information.");
        }
    }

    @GetMapping("/{studentID}/eligibility")
    public boolean checkStudentEligibility(@PathVariable long studentID) {
        try {
            boolean isEligible = studentsService.isEligibleToSelectCourses(studentID);
            logger.info("Checked eligibility for student ID {}: {}", studentID, isEligible);
            return isEligible;
        } catch (UserException e) {
            logger.error("Error checking eligibility for student ID {}: {}", studentID, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error checking eligibility for student ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error checking student eligibility.");
        }
    }

    @GetMapping("/{studentID}/completedCourses")
    public List<Long> getCompletedCourses(@PathVariable long studentID) {
        try {
            List<Long> completedCourses = studentHistoryService.findCompletedCourseIDsByStudentID(studentID);
            logger.info("Retrieved completed courses for student ID {}: {}", studentID, completedCourses);
            return completedCourses;
        } catch (UserException e) {
            logger.error("Error retrieving completed courses for student ID {}: {}", studentID, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving completed courses for student ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error retrieving completed courses.");
        }
    }

    @GetMapping("/{studentID}/grades")
    public List<Double> getStudentGrades(@PathVariable long studentID) {
        try {
            List<Double> grades = studentHistoryService.findGradesByStudentID(studentID);
            logger.info("Retrieved grades for student ID {}: {}", studentID, grades);
            return grades;
        } catch (UserException e) {
            logger.error("Error retrieving grades for student ID {}: {}", studentID, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving grades for student ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error retrieving student grades.");
        }
    }
}