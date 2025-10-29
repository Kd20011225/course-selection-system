package com.example.demo.dao;

import lombok.Data;

@Data
public class Course {
    private Long courseID;
    private String courseName;
    private Long professorID;
    private String professorName;
    private String openSeason;
    private int seatsOpen;
    private String durationPeriod;
    private String prereqCourse;
    private String requireMajor;
}
