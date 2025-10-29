package com.example.demo.mapper;

import com.example.demo.dao.StudentHistory;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface StudentHistoryMapper {
    @Select("SELECT * FROM student_history WHERE studentID = #{studentID}")
    List<StudentHistory> findByStudentID(@Param("studentID") long studentID);

    @Insert("INSERT INTO student_history (studentID, courseID, enrolledTime, finishedTime) " +
            "VALUES (#{studentID}, #{courseID}, #{enrolledTime}, #{finishedTime})")
    void insert(StudentHistory history);

    @Select("SELECT * FROM student_history WHERE studentID = #{studentID} AND courseID = #{courseID}")
    StudentHistory findEnrollment(@Param("studentID") long studentID, @Param("courseID") long courseID);

    @Select("SELECT * FROM student_history WHERE studentID = #{studentID} AND finishedTime IS NOT NULL")
    List<StudentHistory> findCompletedCoursesByStudentID(@Param("studentID") long studentID);

    @Update("UPDATE StudentHistory SET grade = #{grade}, finishedTime = #{finishedTime} WHERE id = #{id}")
    void updateGrade(@Param("id") long id, @Param("grade") double grade, @Param("finishedTime") Timestamp finishedTime);
}
