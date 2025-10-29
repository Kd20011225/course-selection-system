package com.example.demo.dao;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class StudentHistory {

    private Long id;
    private Long studentID;
    private Long courseID;
    private Timestamp enrolledTime;
    private Timestamp finishedTime;
    private Double grade;
}
