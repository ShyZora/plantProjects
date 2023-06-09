package com.education.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.base.exception.EducationException;
import com.education.media.mapper.MediaFilesMapper;
import com.education.media.model.dto.UploadFileParamsDto;
import com.education.media.model.dto.UploadFileResultDto;
import com.education.media.service.MediaFileService;
import com.education.base.model.PageParams;
import com.education.base.model.PageResult;
import com.education.media.model.dto.QueryMediaParamsDto;
import com.education.media.model.po.MediaFiles;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/9/10 8:58
 * @version 1.0
 */
 @Service
public class MediaFileServiceImpl implements MediaFileService {

  @Autowired
  MediaFilesMapper mediaFilesMapper;
 @Autowired
 MinioClient minioClient;
 //普通文件桶
 @Value("${minio.bucket.files}")
 private String bucket_files;
 //获取文件默认存储目录路径 年/月/日
 private String getDefaultFolderPath() {
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  String folder = sdf.format(new Date()).replace("-", "/")+"/";
  return folder;
 }

 //获取文件的md5
 private String getFileMd5(File file) {
  try (FileInputStream fileInputStream = new FileInputStream(file)) {
   String fileMd5 = DigestUtils.md5Hex(fileInputStream);
   return fileMd5;
  } catch (Exception e) {
   e.printStackTrace();
   return null;
  }
 }

 @Override
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

  //构建查询条件对象
  LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
  
  //分页对象
  Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
  // 查询数据内容获得结果
  Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
  // 获取数据列表
  List<MediaFiles> list = pageResult.getRecords();
  // 获取数据总数
  long total = pageResult.getTotal();
  // 构建结果集
  PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
  return mediaListResult;

 }
 public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName){
  //从数据库查询文件
  MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
  if (mediaFiles == null) {
   mediaFiles = new MediaFiles();
   //拷贝基本信息
   BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
   mediaFiles.setId(fileMd5);
   mediaFiles.setFileId(fileMd5);
   mediaFiles.setCompanyId(companyId);
   mediaFiles.setUrl("/" + bucket + "/" + objectName);
   mediaFiles.setBucket(bucket);
   mediaFiles.setFilePath(objectName);
   mediaFiles.setCreateDate(LocalDateTime.now());
   mediaFiles.setAuditStatus("002003");
   mediaFiles.setStatus("1");
   //保存文件信息到文件表
   int insert = mediaFilesMapper.insert(mediaFiles);
   if (insert < 0) {
    //log.error("保存文件信息到数据库失败,{}",mediaFiles.toString());
    EducationException.cast("保存文件信息失败");
   }
  // log.debug("保存文件信息到数据库成功,{}",mediaFiles.toString());

  }
  return mediaFiles;

 }

 private String getMimeType(String extension){
  if(extension==null)
   extension = "";
  //根据扩展名取出mimeType
  ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
  //通用mimeType，字节流
  String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
  if(extensionMatch!=null){
   mimeType = extensionMatch.getMimeType();
  }
  return mimeType;
 }
 public boolean addMediaFilesToMinIO(String localFilePath,String mimeType,String bucket, String objectName) {
  try {
   System.out.println("桶的名字"+bucket);
   UploadObjectArgs testbucket = UploadObjectArgs.builder()
           .bucket(bucket)
           .object(objectName)
           .filename(localFilePath)
           .contentType(mimeType)
           .build();
   System.out.println(testbucket);
   minioClient.uploadObject(testbucket);
   //log.debug("上传文件到minio成功,bucket:{},objectName:{}",bucket,objectName);
   System.out.println("上传成功");
   return true;
  } catch (Exception e) {
   e.printStackTrace();
   //log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}",bucket,objectName,e.getMessage(),e);
   EducationException.cast("上传文件到文件系统失败");
  }
  return false;
 }

 @Override
 public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath) {
  File file = new File(localFilePath);
  if (!file.exists()) {
   EducationException.cast("文件不存在");
  }
  //文件名称
  String filename = uploadFileParamsDto.getFilename();
  //文件扩展名
  String extension = filename.substring(filename.lastIndexOf("."));
  //文件mimeType
  String mimeType = getMimeType(extension);
  //文件的md5值
  String fileMd5 = getFileMd5(file);
  //文件的默认目录
  String defaultFolderPath = getDefaultFolderPath();
  //存储到minio中的对象名(带目录)
  String  objectName = defaultFolderPath + fileMd5 + extension;
  //将文件上传到minio
  boolean b = addMediaFilesToMinIO(localFilePath, mimeType, bucket_files, objectName);
  //文件大小
  uploadFileParamsDto.setFileSize(file.length());
  //将文件信息存储到数据库
  MediaFiles mediaFiles = addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_files, objectName);
  //准备返回数据
  UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
  BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
  return uploadFileResultDto;

 }

}
