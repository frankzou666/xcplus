package com.example.content.api;


import com.example.content.model.dto.BindTeachPlanDto;
import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.content.model.po.TeachplanMedia;
import com.example.xcpluscontentservice.content.service.TeachplanMediaService;
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

    @Autowired
    TeachplanMediaService teachplanMediaService;


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

    @DeleteMapping("/teachplan/{courseId}")
    public void deleteTeachPlan(@PathVariable Long courseId) {
        teachplanService.deleteTeachPlan(courseId);

    }


    @PostMapping("/teachplan/association/media")
    public TeachplanMedia assocTeachPlan(@RequestBody BindTeachPlanDto bindTeachPlanDto) {
        /*
         *  课程计划称动
         *
         */
        TeachplanMedia teachplanMedia = teachplanMediaService.bindTeachPlan(bindTeachPlanDto);
        return  teachplanMedia;

    }

    @PostMapping("/teachplan/{moveType}/{Id}")
    public void moveTeachPlan(@PathVariable String moveType,@PathVariable Long Id) {
        /*
         *  课程计划称动
         *
         */

        teachplanService.moveTeachPlan(moveType,Id);


    }


}
