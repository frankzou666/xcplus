package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.content.model.po.Teachplan;
import com.example.xcpluscontentservice.content.mapper.TeachplanMapper;
import com.example.xcpluscontentservice.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Slf4j
@Service
public class TeachplanServiceImpl  implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;
    @Override
    public List<TeachplanDto> queryTreeNodes(Long courseId) {
        List<TeachplanDto>  teachplanDtoNodes= teachplanMapper.selectTreeNodes(courseId);
        return  teachplanDtoNodes;
    }

    @Override
    public void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto) {
        Long teachPlanId = saveTeachPlanDto.getId();
        //新增
        if (teachPlanId == null) {
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachPlanDto, teachplan);
            //根据courseId,parentId 来设置orderBy字段
            Long courseId = saveTeachPlanDto.getCourseId();
            Long parentId = saveTeachPlanDto.getParentid();

            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper  = queryWrapper.eq(Teachplan::getCourseId,courseId).eq(Teachplan::getParentid,parentId);

            //新增要把自己的id=Parentid

            Integer count = teachplanMapper.selectCount(queryWrapper);
            teachplan.setOrderby(count + 1);

            teachplanMapper.insert(teachplan);
        } else {
            Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
            BeanUtils.copyProperties(saveTeachPlanDto, teachplan);
            teachplanMapper.updateById(teachplan);


        }

    }
}
