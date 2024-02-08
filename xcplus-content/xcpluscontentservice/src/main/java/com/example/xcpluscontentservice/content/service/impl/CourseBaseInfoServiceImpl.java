package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentmodel.content.model.dto.QueryCourseParamsDto;
import com.example.xcpluscontentmodel.content.model.po.CourseBase;
import com.example.xcpluscontentservice.content.mapper.CourseBaseMapper;
import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
     CourseBaseMapper courseBaseMapper;
    @Override
    public PageResult<CourseBase> queryCousrBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        Page<CourseBase> page=new Page<>(pageParams.getPageNo(),pageParams.getPageSize());
        Page<CourseBase> result = courseBaseMapper.selectPage(page,queryWrapper);
        List<CourseBase> courBaseList = result.getRecords();
        PageResult<CourseBase> pageResult = new PageResult<>(courBaseList, result.getTotal(), result.getCurrent(), result.getSize());
        return  pageResult;
    }
}
