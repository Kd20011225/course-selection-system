package com.example.demo.mapper;

import com.example.demo.dao.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentsMapper {

    @Select("SELECT * FROM students WHERE studentID = #{studentID}")
    Student findById(@Param("studentID") long studentID);

    @Update("UPDATE students SET "
          + "firstName = #{firstName}, "
          + "lastName = #{lastName}, "
          + "userName = #{userName}, "
          + "email = #{email}, "
          + "gender = #{gender}, "
          + "phoneNumber = #{phoneNumber}, "
          + "dateOfBirth = #{dateOfBirth}, "
          + "yearLevel = #{yearLevel}, "
          + "score = #{score}, "
          + "major = #{major}, "
          + "status = #{status} "
          + "WHERE studentID = #{studentID}")
    void updateStudent(Student student);

    @Update("UPDATE students SET status = #{status} WHERE studentID = #{studentID}")
    void updateStudentStatus(@Param("studentID") long studentID, @Param("status") int status);

    @Update("UPDATE students SET score = #{score} WHERE studentID = #{studentID}")
    void updateAverageScore(@Param("studentID") long studentID, @Param("score") double score);

    @Select("SELECT * FROM students")
    List<Student> findAll();
}
