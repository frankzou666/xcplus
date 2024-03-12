package com.example.content.model.dto;


import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Slf4j
@Data
public class BindTeachPlanDto {

    //文件名称
    @NotEmpty(message = "文件名称不能为空")
    public  String fileName;

    //媒资id
    @NotEmpty(message = "媒资id不能为空")
    public String mediaId;

    //课程计划id
    @NotEmpty(message = "课程计划id不能为空")
    private Long teachplanId;



}
