package com.example.content.model.dto;

import com.example.content.model.po.Teachplan;
import com.example.content.model.po.TeachplanMedia;
import groovy.util.logging.Slf4j;
import lombok.Data;

import java.util.List;


/*
*
*   课程计划
*
* */

@Data
@Slf4j
public class TeachplanDto extends Teachplan {

    public TeachplanMedia teachplanMedia;

    public List<TeachplanDto> teachPlanTreeNodes;

}
