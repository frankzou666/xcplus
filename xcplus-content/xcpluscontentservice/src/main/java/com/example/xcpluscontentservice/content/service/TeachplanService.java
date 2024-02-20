package com.example.xcpluscontentservice.content.service;

import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;

import java.util.List;

public interface TeachplanService {

    public   List<TeachplanDto> queryTreeNodes(Long courseId);


    //新增或修改课程计划
    public   void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto);

}
