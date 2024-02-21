package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.content.model.po.CourseTeacher;
import com.example.xcpluscontentservice.content.mapper.CourseTeacherMapper;
import com.example.xcpluscontentservice.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    CourseTeacherMapper courseTeacherMapper;
    @Override
    public List<CourseTeacher> findTeachersByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper =queryWrapper.eq(CourseTeacher::getCourseId,courseId);
        List<CourseTeacher> courseTeacherList = courseTeacherMapper.selectList(queryWrapper);
        return courseTeacherList;

    }

    @Transactional
    @Override
    public CourseTeacher addorUpdateCourseTeacher(CourseTeacher courseTeacher) {
        //如果有id表示修改，否则表示新增
        Long id = courseTeacher.getId();
        if (id !=null) {
            CourseTeacher courseTeacherNew = courseTeacherMapper.selectById(id);
            BeanUtils.copyProperties(courseTeacher,courseTeacherNew);
            courseTeacherMapper.updateById(courseTeacherNew);
            return  courseTeacherNew;
        } else {
            CourseTeacher courseTeacherNew = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher,courseTeacherNew);
            courseTeacherMapper.insert(courseTeacherNew);
            return  courseTeacherNew;

        }
    }


    @Transactional
    @Override
    public void deleteCourseTeacher(Long courseId, Long id) {
        //删除老师
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper =queryWrapper.eq(CourseTeacher::getCourseId,courseId).eq(CourseTeacher::getId,id);
        courseTeacherMapper.delete(queryWrapper);
    }
}
