package com.example.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

    @Select("select * from media_process " +
            "where id % #{shardTotal}= #{shardIndex} " +
            "and status in (1,3)"
             )
    List<MediaProcess>  getMediaProcess(@Param("shardIndex") int shardIndex, @Param("shardTotal") int shardTotal);

}
