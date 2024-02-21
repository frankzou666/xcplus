package com.example.xcpluscontentservice.content.service;

import com.example.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {

    public List<CourseTeacher> findTeachersByCourseId(Long courseId);

    public CourseTeacher addorUpdateCourseTeacher(CourseTeacher courseTeacher);

    public void deleteCourseTeacher(Long courseId,Long id);

}
