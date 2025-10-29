package com.example.demo.service;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentHistory;
import com.example.demo.mapper.StudentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentsServiceImpl implements StudentsService {

    private static final Logger logger = LoggerFactory.getLogger(StudentsServiceImpl.class);

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private StudentHistoryService studentHistoryService;

    @Override
    public Student getStudentByID(long studentID) {
        try {
            return studentsMapper.findById(studentID);
        } catch (Exception e) {
            throw new UserException("Error retrieving student.");
        }
    }

    @Override
    public void updateStudent(Student student) {
        try {
            studentsMapper.updateStudent(student);
        } catch (Exception e) {
            throw new UserException("Error updating student.");
        }
    }

    @Override
    public boolean isEligibleToSelectCourses(long studentID) {
        try {
            Student student = studentsMapper.findById(studentID);
            return student != null && student.getStatus() == 1;
        } catch (Exception e) {
            throw new UserException("Error checking student eligibility.");
        }
    }

    @Override
    public void updateAverageScore(long studentID) {
        try {
            List<StudentHistory> histories = studentHistoryService.findStudentByID(studentID);
            double average = histories.stream()
                    .filter(h -> h.getGrade() != null)
                    .mapToDouble(StudentHistory::getGrade)
                    .average()
                    .orElse(0.0);
            studentsMapper.updateAverageScore(studentID, average);
        } catch (Exception e) {
            throw new UserException("Error updating average score.");
        }
    }

    @Override
    public void updateStudentStatus(long studentID, int status) {
        try {
            studentsMapper.updateStudentStatus(studentID, status);
            logger.info("Updated status for student with ID {}: {}", studentID, status);
        } catch (Exception e) {
            logger.error("Error updating status for student with ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error updating student status: " + e.getMessage());
        }
    }

    @Override
    public List<Student> findAll() {
        try {
            return studentsMapper.findAll();
        } catch (Exception e) {
            throw new UserException("Error retrieving students.");
        }
    }

    @Override
    public double calculateAverageScore(long studentID) {
        try {
            List<Double> grades = studentHistoryService.findGradesByStudentID(studentID);
            if (grades == null || grades.isEmpty()) {
                logger.warn("No grades found for student with ID {}.", studentID);
                return 0.0;
            }
            double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            logger.info("Calculated average score for student with ID {}: {}", studentID, average);
            return average;
        } catch (Exception e) {
            logger.error("Error calculating average score for student with ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error calculating average score.");
        }
    }
}
