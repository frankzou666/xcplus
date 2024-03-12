package com.example.content.model.dto;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Data
@Slf4j
//用于课程预类
public class CoursePreviewDto {

    //课程基本信息
    private  CourseBaseInfoDto courseBase;

    //课程计划信息
    private  List<TeachplanDto> teachplans;

    //课程信息

    //课程资资信息


}
