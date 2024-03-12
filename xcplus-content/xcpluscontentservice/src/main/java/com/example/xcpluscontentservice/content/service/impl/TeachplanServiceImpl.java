package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.content.model.dto.BindTeachPlanDto;
import com.example.content.model.dto.SaveTeachPlanDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.content.model.po.Teachplan;
import com.example.xcplusbase.base.exception.XcplusException;
import com.example.xcpluscontentservice.content.mapper.TeachplanMapper;
import com.example.xcpluscontentservice.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

            //最后一个sortBy 再加1
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper  = queryWrapper.eq(Teachplan::getCourseId,courseId).eq(Teachplan::getParentid,parentId);
            queryWrapper.orderByAsc(Teachplan::getOrderby);
            List<Teachplan> teachplanOrderby = teachplanMapper.selectList(queryWrapper);

            //Integer count = teachplanMapper.selectCount(queryWrapper);
            // 得到最后一个对像的orderBy,如果是新增的小章或是小节就是1
            Integer count = 0;
            if (teachplanOrderby.size()>=1) {
                count = teachplanOrderby.get(teachplanOrderby.size()-1).getOrderby() ;
            }
            teachplan.setOrderby(count+1);
            teachplanMapper.insert(teachplan);
        } else {
            Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
            BeanUtils.copyProperties(saveTeachPlanDto, teachplan);
            teachplanMapper.updateById(teachplan);


        }

    }

    @Override
    public void deleteTeachPlan(Long courseId) {
        //如果当前是小章节，可以直接删
        Teachplan teachplan = teachplanMapper.selectById(courseId);
        //说明是小章节
        if (teachplan.getParentid()!=0){
            teachplanMapper.deleteById(courseId);
        } else {
            Long parentId = teachplan.getParentid();
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper  = queryWrapper.eq(Teachplan::getParentid,courseId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            //如果count>0说明有子节点，不能删，抛异常，只有是没有子章节的大章才能删；
            if (count==0) {
                teachplanMapper.deleteById(courseId);
            } else {
                XcplusException.cast("120409");
            }

        }

        //如果不是，则看是不是还有小章节，如果没有可以删，否则不能删

    }

    @Override
    @Transactional
    public void moveTeachPlan(String moveType, Long Id) {

        Teachplan teachplan = teachplanMapper.selectById(Id);
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper  = queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
        queryWrapper.orderByAsc(Teachplan::getOrderby);
        List<Teachplan> teachplanList = teachplanMapper.selectList(queryWrapper);

        //把parentid=0的找出来
        List<Teachplan> teachplanListParent = teachplanList.stream().filter(item->item.getParentid()==0).collect(Collectors.toList());

        //把相同的parentId都找出来
        List<Teachplan> teachplanListChildRen = teachplanList.stream().filter(item->item.getParentid().equals(teachplan.getParentid())).collect(Collectors.toList());
        //看是上移还是下移
        if (moveType.equals("moveup")) {
            //是大章还是小节
            if (teachplan.getParentid().equals("0")) {
                if (teachplanListParent.size()>=2) {
                    updateOrderBy(teachplan,teachplanListParent);
                    return;
                }
            } else {
                if (teachplanListChildRen.size()>=2) {
                    updateOrderBy(teachplan,teachplanListChildRen);
                    return;
                }
            }

        } else if (moveType.equals("movedown")) {
            if (teachplan.getParentid().equals("0")) {
                if (teachplanListParent.size()>=2) {
                    updateOrderByMovedown(teachplan,teachplanListParent);
                    return;
                }
            } else {
                if (teachplanListChildRen.size()>=2) {
                    updateOrderByMovedown(teachplan,teachplanListChildRen);
                    return;
                }
            }
        } else {

        }
    }




    public void updateOrderByMovedown(Teachplan teachplan,List<Teachplan> teachplanList) {
        //得到数组长度
        int teachplanListLengh = teachplanList.size();
        //如果已经是最后一个了，就不用排了
        if ( ! teachplan.getId().equals(teachplanList.get(teachplanListLengh-1).getId())) {
            //得到我自己当前的位置
            int teachplanIndex =teachplanList.indexOf(teachplan);
            //得到下一个对像
            Teachplan teachplanPrev = teachplanList.get(teachplanIndex+1);
            //交换两个变量的orderBy
            swapTeachPlanOrderby(teachplanPrev,teachplan);

        }

    }

    //更新updateOrderBy
    public void updateOrderBy(Teachplan teachplan,List<Teachplan> teachplanList) {
        if (! teachplan.getId().equals(teachplanList.get(0).getId())) {
            //得到我自己当前的位置
            int teachplanIndex =teachplanList.indexOf(teachplan);
            //得到前一个对像
            Teachplan teachplanPrev = teachplanList.get(teachplanIndex-1);
            //交换两个变量的orderBy
            swapTeachPlanOrderby(teachplanPrev,teachplan);

        }

    }


    //交换对像的sortby,并更新
    public void swapTeachPlanOrderby(Teachplan teachplanPrev,Teachplan teachplan) {
        int tmpSortBy = teachplanPrev.getOrderby();
        teachplanPrev.setOrderby(teachplan.getOrderby());
        teachplan.setOrderby(tmpSortBy);
        //两个同时更新数据库
        teachplanMapper.updateById(teachplanPrev);
        teachplanMapper.updateById(teachplan);
    }




}
