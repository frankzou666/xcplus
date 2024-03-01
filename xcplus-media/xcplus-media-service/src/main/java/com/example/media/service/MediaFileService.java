package com.example.media.service;

import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.media.model.dto.QueryMediaParamsDto;
import com.example.media.model.dto.UploadFileParamsDto;
import com.example.media.model.dto.UploadFileResultDto;
import com.example.media.model.po.MediaFiles;
import com.example.xcplusbase.base.model.RestResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return com.example.base.model.PageResult<com.example.media.model.po.MediaFiles>
  * @author Mr.M
  * @date 2022/9/10 8:57
 */
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件
  * @param companyId 机构id
  * @param uploadFileParamsDto 文件信息
  * @param localFilePath 文件本地路径
  * @return UploadFileResultDto
  */
 public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);

 public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName);

 public Boolean checkfile( String fileMd5) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

 public Boolean checkChunk( String fileMd5,int chunk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

 public RestResponse uploadchunk(String localFilePath, String fileMd5, int chunk);

 public RestResponse mergechunks(String fileMd5,String fileName,int chunkTotal);

}
