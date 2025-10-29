package com.example.demo.service;

import com.example.demo.businessExceptions.UserException;
import com.example.demo.cache.PrereqCache;
import com.example.demo.dao.Course;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentHistory;
import com.example.demo.mapper.CoursesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursesServiceImpl implements CoursesService {

    private static final Logger logger = LoggerFactory.getLogger(CoursesServiceImpl.class);

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private StudentHistoryService studentHistoryService;

    @Autowired
    private PrereqCache prereqCache;

    @Override
    public Course getCourseByID(long courseID) {
        try {
            Course course = coursesMapper.findByCourseID(courseID);
            if (course == null) {
                logger.warn("Course {} not found", courseID);
                throw new UserException("Course not found.");
            }
            return course;
        } catch (Exception e) {
            logger.error("Error retrieving course with ID {}: {}", courseID, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try {
            return coursesMapper.getAllCourses();
        } catch (Exception e) {
            logger.error("Error retrieving all courses: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void addCourse(Course course) {
        try {
            coursesMapper.insert(course);

            // Update PrereqCache
            if (course.getPrereqCourse() != null && !course.getPrereqCourse().isEmpty()) {
                List<Long> prereqCourses = Arrays.stream(course.getPrereqCourse().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                prereqCache.addCoursePrerequisites(course.getCourseID(), prereqCourses);
            }
        } catch (Exception e) {
            logger.error("Error adding course: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCourseSeats(long courseID, int seatsOpen) {
        try {
            coursesMapper.updateSeats(courseID, seatsOpen);
        } catch (Exception e) {
            logger.error("Error updating course seats for courseID {}: {}", courseID, e.getMessage());
            throw e;
        }
    }

    @Override
    public String selectCourse(long courseID, long studentID) {
        try {
            logger.info("Student {} attempting to enroll in course {}", studentID, courseID);

            // Fetch course and student
            Course course = getCourseByID(courseID);
            Student student = studentsService.getStudentByID(studentID);
            if (student == null) {
                logger.warn("Student {} not found", studentID);
                throw new UserException("Student not found.");
            }

            // Check if student is eligible
            if (!studentsService.isEligibleToSelectCourses(studentID)) {
                logger.warn("Student {} is not eligible to select courses", studentID);
                throw new UserException("Student is not eligible to select courses.");
            }

            // Check if student already enrolled
            if (studentHistoryService.isStudentEnrolledInCourse(studentID, courseID)) {
                logger.warn("Student {} is already enrolled in course {}", studentID, courseID);
                throw new UserException("Student is already enrolled in this course.");
            }

            // Use PrereqCache to check prerequisites
            if (prereqCache.containsCourse(courseID)) {
                List<Long> prereqCourses = prereqCache.getPrerequisites(courseID);
                if (prereqCourses != null && !prereqCourses.isEmpty()) {
                    List<Long> completedCourses = studentHistoryService.findCompletedCourseIDsByStudentID(studentID);
                    if (!completedCourses.containsAll(prereqCourses)) {
                        logger.warn("Student {} has not completed the prerequisite courses for course {}", studentID, courseID);
                        throw new UserException("Student has not completed the prerequisite courses.");
                    }
                }
            } else {
                logger.info("Course {} has no prerequisites in PrereqCache. Skipping prerequisite check.", courseID);
            }

            // Check major requirement
            if (course.getRequireMajor() != null && !course.getRequireMajor().isEmpty()) {
                List<String> requiredMajors = Arrays.asList(course.getRequireMajor().split(","));
                if (!requiredMajors.contains(student.getMajor())) {
                    logger.warn("Student {}'s major does not match the required majors for course {}", studentID, courseID);
                    throw new UserException("Course is restricted to certain majors.");
                }
            }

            // Check available seats
            if (course.getSeatsOpen() <= 0) {
                logger.warn("No seats available for course {}", courseID);
                throw new UserException("No seats available.");
            }

            // Enroll student
            StudentHistory history = new StudentHistory();
            history.setStudentID(studentID);
            history.setCourseID(courseID);
            history.setEnrolledTime(new Timestamp(System.currentTimeMillis()));
            studentHistoryService.addHistory(history);

            // Update course seats
            updateCourseSeats(courseID, course.getSeatsOpen() - 1);

            logger.info("Student {} enrolled in course {} successfully", studentID, courseID);
            return "Student enrolled successfully.";

        } catch (UserException e) {
            logger.error("Enrollment error: {}", e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Error selecting course", e);
            return "An error occurred while selecting the course.";
        }
    }

    @Override
    public void updateCourse(Course course) {
        try {
            coursesMapper.updateCourse(course);

            // Update PrereqCache
            if (course.getPrereqCourse() != null && !course.getPrereqCourse().isEmpty()) {
                List<Long> prereqCourses = Arrays.stream(course.getPrereqCourse().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                prereqCache.addCoursePrerequisites(course.getCourseID(), prereqCourses);
            } else {
                prereqCache.removeCourse(course.getCourseID());
            }
        } catch (Exception e) {
            logger.error("Error updating course: {}", e.getMessage());
            throw new UserException("Error updating course.");
        }
    }
}
