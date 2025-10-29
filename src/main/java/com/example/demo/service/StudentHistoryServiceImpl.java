package com.example.demo.service;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.dao.StudentHistory;
import com.example.demo.mapper.StudentHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentHistoryServiceImpl implements StudentHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(StudentHistoryServiceImpl.class);

    @Autowired
    private StudentHistoryMapper historyMapper;

    @Override
    public List<StudentHistory> findStudentByID(long studentID) {
        try {
            List<StudentHistory> histories = historyMapper.findByStudentID(studentID);
            if (histories == null || histories.isEmpty()) {
                logger.warn("No history found for studentID {}", studentID);
                throw new UserException("No history found for the student.");
            }
            return histories;
        } catch (Exception e) {
            logger.error("Error retrieving student history for studentID {}: {}", studentID, e.getMessage());
            throw e;
        }
    }

    @Override
    public void addHistory(StudentHistory history) {
        try {
            historyMapper.insert(history);
            logger.info("Added history record for studentID {} and courseID {}", history.getStudentID(), history.getCourseID());
        } catch (Exception e) {
            logger.error("Error adding history record: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isStudentEnrolledInCourse(long studentID, long courseID) {
        try {
            StudentHistory history = historyMapper.findEnrollment(studentID, courseID);
            return history != null;
        } catch (Exception e) {
            logger.error("Error checking enrollment for studentID {} and courseID {}: {}", studentID, courseID, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Long> findCompletedCourseIDsByStudentID(long studentID) {
        try {
            List<StudentHistory> histories = historyMapper.findCompletedCoursesByStudentID(studentID);
            if (histories == null || histories.isEmpty()) {
                logger.warn("No completed courses found for studentID {}", studentID);
                return List.of(); // Return empty list
            }
            return histories.stream()
                    .map(StudentHistory::getCourseID)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving completed courses for studentID {}: {}", studentID, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Double> findGradesByStudentID(long studentID) {
        try {
            List<StudentHistory> histories = historyMapper.findCompletedCoursesByStudentID(studentID);
            if (histories == null || histories.isEmpty()) {
                logger.warn("No grades found for studentID {}", studentID);
                return List.of(); // Return empty list
            }
            return histories.stream()
                    .map(StudentHistory::getGrade)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving grades for studentID {}: {}", studentID, e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateGrade(long id, double grade) {
        try {
            Timestamp finishedTime = new Timestamp(System.currentTimeMillis());
            historyMapper.updateGrade(id, grade, finishedTime);
            logger.info("Updated grade for historyID {}: grade={}, finishedTime={}", id, grade, finishedTime);
        } catch (Exception e) {
            logger.error("Error updating grade for historyID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public double calculateAverageGrade(long studentID) {
        try {
            List<Double> grades = findGradesByStudentID(studentID);
            if (grades.isEmpty()) {
                logger.warn("No grades available to calculate average for studentID {}", studentID);
                return 0.0;
            }
            double sum = grades.stream().mapToDouble(Double::doubleValue).sum();
            double average = sum / grades.size();
            logger.info("Calculated average grade for studentID {}: {}", studentID, average);
            return average;
        } catch (Exception e) {
            logger.error("Error calculating average grade for studentID {}: {}", studentID, e.getMessage());
            throw e;
        }
    }
}
