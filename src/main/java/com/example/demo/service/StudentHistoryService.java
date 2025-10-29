package com.example.demo.service;

import com.example.demo.dao.StudentHistory;

import java.util.List;

public interface StudentHistoryService {

    List<StudentHistory> findStudentByID(long studentId);
    void addHistory(StudentHistory history);
    boolean isStudentEnrolledInCourse(long studentID, long classID);
    List<Long> findCompletedCourseIDsByStudentID(long studentD);
    List<Double> findGradesByStudentID(long studentID);
    void updateGrade(long studentID, double grade);
    double calculateAverageGrade(long studentID);
}
