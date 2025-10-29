package com.example.demo.cache;

import com.example.demo.dao.Course;
import com.example.demo.mapper.CoursesMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class PrereqCache {
    private static final Logger logger = LoggerFactory.getLogger(PrereqCache.class);

    @Autowired
    private CoursesMapper coursesMapper;

    private Map<Long, List<Long>> prereqMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadPrerequisites();
    }

    public void loadPrerequisites() {
        try {
            List<Course> courses = coursesMapper.getAllCourses();
            for (Course course : courses) {
                if (course.getPrereqCourse() != null && !course.getPrereqCourse().isEmpty()) {
                    List<Long> prereqCourses = Arrays.stream(course.getPrereqCourse().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    prereqMap.put(course.getCourseID(), prereqCourses);
                }
            }
            logger.info("Prerequisite cache loaded with {} courses", prereqMap.size());
        } catch (Exception e) {
            logger.error("Error loading prerequisites into cache", e);
        }
    }

    public List<Long> getPrerequisites(Long courseID) {
        return prereqMap.getOrDefault(courseID, Collections.emptyList());
    }

    public void addCoursePrerequisites(Long courseID, List<Long> prerequisites) {
        prereqMap.put(courseID, prerequisites);
        logger.info("Prerequisites for course {} added/updated in cache", courseID);
    }

    public void removeCourse(Long courseID) {
        prereqMap.remove(courseID);
        logger.info("Course {} removed from prerequisite cache", courseID);
    }

    public boolean containsCourse(Long courseID) {
        return prereqMap.containsKey(courseID);
    }
}
