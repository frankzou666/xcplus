package com.example.xcpluscontentservice.content.service;

import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentmodel.content.model.dto.QueryCourseParamsDto;
import com.example.xcpluscontentmodel.content.model.po.CourseBase;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface CourseBaseInfoService {
    public PageResult<CourseBase> queryCousrBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
