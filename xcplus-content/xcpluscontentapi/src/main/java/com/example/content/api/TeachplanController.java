package com.example.content.api;


import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.xcpluscontentservice.content.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
* 课程计划管理
 */
@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;


    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTeachplanNodes(@PathVariable Long courseId) {
        //查询课程接口
        List<TeachplanDto>  teachplanDtoNodes= teachplanService.queryTreeNodes(courseId);
        return  teachplanDtoNodes;
    }

    @PostMapping("/teachplan")
    public void saveTeachPlan(@RequestBody SaveTeachPlanDto saveTeachPlanDto) {
        //查询课程接口
        teachplanService.saveTeachPlan(saveTeachPlanDto);
    }
}
