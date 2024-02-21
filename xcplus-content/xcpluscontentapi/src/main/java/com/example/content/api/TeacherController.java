package com.example.content.api;


import com.example.content.model.po.CourseTeacher;
import com.example.xcpluscontentservice.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TeacherController {

    //根据课程ID返回老师

    @Autowired
    CourseTeacherService courseTeacherService;

    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getTeacherByCourseId(@PathVariable  Long courseId){
        List<CourseTeacher> courseTeacherList = courseTeacherService.findTeachersByCourseId(courseId);
        return  courseTeacherList;
    }

    @PostMapping("/courseTeacher")
    public CourseTeacher saveTeacher(@RequestBody CourseTeacher courseTeacher){
        CourseTeacher courseTeacherNew = courseTeacherService.addorUpdateCourseTeacher(courseTeacher);
        return  courseTeacher;
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteTeacher(@PathVariable  Long courseId,@PathVariable  Long id){
        courseTeacherService.deleteCourseTeacher(courseId,id);
    }
}
