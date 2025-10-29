package com.example.demo.controller;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.dao.StudentHistory;
import com.example.demo.service.StudentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/students/{studentID}/history")
public class StudentHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(StudentHistoryController.class);

    @Autowired
    private StudentHistoryService studentHistoryService;

    @GetMapping
    public List<StudentHistory> getStudentHistory(@PathVariable long studentID) {
        try {
            List<StudentHistory> histories = studentHistoryService.findStudentByID(studentID);
            logger.info("Retrieved history for student ID {}.", studentID);
            return histories;
        } catch (UserException e) {
            logger.error("Error retrieving history for student ID {}: {}", studentID, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving history for student ID {}: {}", studentID, e.getMessage());
            throw new UserException("Error retrieving student history.");
        }
    }

    @PostMapping
    public String addHistoryRecord(@PathVariable long studentID, @RequestBody StudentHistory history) {
        try {
            history.setStudentID(studentID);
            studentHistoryService.addHistory(history);
            logger.info("Added history record for student ID {}.", studentID);
            return "History record added successfully.";
        } catch (UserException e) {
            logger.error("Error adding history record for student ID {}: {}", studentID, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error adding history record for student ID {}: {}", studentID, e.getMessage());
            return "Error adding history record.";
        }
    }

    @PutMapping("/{historyID}/grade")
    public String updateGrade(@PathVariable long studentID, @PathVariable long historyID, @RequestBody double grade) {
        try {
            studentHistoryService.updateGrade(historyID, grade);
            logger.info("Updated grade for history ID {} of student ID {}.", historyID, studentID);
            return "Grade updated successfully.";
        } catch (UserException e) {
            logger.error("Error updating grade for history ID {}: {}", historyID, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error updating grade for history ID {}: {}", historyID, e.getMessage());
            return "Error updating grade.";
        }
    }
}
