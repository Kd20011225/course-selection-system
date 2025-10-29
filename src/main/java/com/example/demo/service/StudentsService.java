package com.example.demo.service;

import com.example.demo.dao.Student;

import java.util.List;


public interface StudentsService {
    Student getStudentByID(long studentID);

    void updateStudent(Student student);

    boolean isEligibleToSelectCourses(long studentID);

    void updateAverageScore(long studentID);

    void updateStudentStatus(long studentID, int status);

    List<Student> findAll();

    double calculateAverageScore(long studentID);
}
