package com.example.xcpluscontentservice.content.service;

import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TeachplanService {

    public   List<TeachplanDto> queryTreeNodes(Long courseId);


    //新增或修改课程计划
    public   void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto);

    public   void deleteTeachPlan(Long courseId);

    //移动课程
    public void moveTeachPlan(@PathVariable String moveType, @PathVariable Long Id);

}
