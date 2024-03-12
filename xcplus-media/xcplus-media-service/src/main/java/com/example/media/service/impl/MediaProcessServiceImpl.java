package com.example.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.media.mapper.MediaProcessMapper;
import com.example.media.model.po.MediaProcess;
import com.example.media.service.MediaProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MediaProcessServiceImpl implements MediaProcessService {

    @Autowired
    MediaProcessMapper mediaProcessMapper ;
    @Override
    public List<MediaProcess> getMediaProcess(int shardIndex,int shardTotal) {
        List<MediaProcess> mediaProcessList = mediaProcessMapper.getMediaProcess(shardIndex,shardTotal);
        return  mediaProcessList;
    }

    @Override
    public MediaProcess findMediaProcess(long id) {
        MediaProcess mediaProcess = mediaProcessMapper.selectById(id);
        if (mediaProcess != null){
            return  mediaProcess;
        } else {
            return  null;
        }
    }

    @Override
    public void saveMediaProcess(MediaProcess mediaProcess) {
        mediaProcessMapper.updateById(mediaProcess);

    }


}
