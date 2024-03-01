package com.example.media.api;

import com.example.xcplusbase.base.model.RestResponse;
import com.example.media.model.dto.UploadFileParamsDto;
import com.example.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Mr.M
 * @version 1.0
 * @description 上传视频
 * @date 2023/2/18 10:34
 */

@Api(value = "大文件上传接口", tags = "大文件上传接口")
@RestController
public class BigFilesController {

    @Autowired
    MediaFileService mediaFileService;


    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkfile(
            @RequestParam("fileMd5") String fileMd5
    ) throws Exception {

        //返果返回真，表示文件存在
        if (mediaFileService.checkfile(fileMd5)){
            return  RestResponse.success(true);
        } else {
            return  RestResponse.validfail("失败");
        }
    }


    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) throws Exception {

        //返果返回真，表示文件存在
        if (mediaFileService.checkChunk(fileMd5,chunk)){
            return  RestResponse.success(true);
        } else {
            return  RestResponse.validfail("失败");
        }
    }

    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws Exception {

        //创建一个临时文件
        File tempFile = File.createTempFile("minio", ".temp");
        file.transferTo(tempFile);
        //文件路径
        String localFilePath = tempFile.getAbsolutePath();
        RestResponse restResponse = mediaFileService.uploadchunk(localFilePath,fileMd5,chunk);

        return restResponse;
    }

    @ApiOperation(value = "合并文件")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {
        Long companyId = 1232141425L;
        RestResponse restResponse = mediaFileService.mergechunks(fileMd5, fileName,  chunkTotal);
        return  restResponse;

    }


}
