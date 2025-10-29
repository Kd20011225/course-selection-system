package com.example.demo.dao;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Student {
    private Long studentID;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String gender;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String yearLevel;
    private double score;
    private String major;
    private int status;
}

