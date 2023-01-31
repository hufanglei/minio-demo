package com.jsu.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jsu.config.MinioClientUtils;
import com.jsu.config.MinioConfig;
import com.jsu.constant.MinioConstant;
import com.jsu.entity.MinioFile;
import com.jsu.vo.ApiResult;
import com.jsu.vo.MinioResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cn.hutool.core.io.FileUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@Api(tags = "文件处理模块")
public class FileHandleController {

    @Autowired
    private MinioClientUtils minioClientUtils;
    @Autowired
    private MinioConfig minioConfig;

    @PostMapping(value = {"/uploadFile"})
    @ApiOperation(value = "上传文件,支持批量上传")
    @ApiImplicitParam(name = "files",value = "文件对象",dataType = "File")
    public ApiResult uploadFile(@RequestParam("files") List<MultipartFile> files) {
        log.info(files.toString());
        if (CollectionUtils.isEmpty(files)) {
            return ApiResult.error("未选择文件！");
        }

        List<MinioResponseDTO> MinioResponseDTOList = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
//            获取文件拓展名
            String extName = FileUtil.extName(originalFilename);
            log.info("文件拓展名:" + extName);
//            生成新的文件名，存入到minio
            long millSeconds = Instant.now().toEpochMilli();
            String minioFileName = millSeconds + RandomStringUtils.randomNumeric(12) + "." + extName;
            String contentType = file.getContentType();
            log.info("文件mime:{}", contentType);
//            返回文件大小,单位字节
            long size = file.getSize();
            log.info("文件大小：" + size);
            try {
                String bucketName = minioConfig.getBucketName();
                minioClientUtils.putObject(bucketName, file, minioFileName);
                String fileUrl = minioClientUtils.getObjectUrl(bucketName, minioFileName);
                MinioFile minioFile = new MinioFile();
                minioFile.setOriginalFileName(originalFilename);
                minioFile.setFileExtName(extName);
                minioFile.setFileName(minioFileName);
                minioFile.setFileSize(size);
                minioFile.setMime(contentType);
                minioFile.setIsDelete(NumberUtils.INTEGER_ZERO);
                minioFile.setFileUrl(fileUrl);
                boolean insert = minioFile.insert();
                if (insert) {//文件地址写入数据库
                    MinioResponseDTO minioResponseDTO = new MinioResponseDTO();
                    minioResponseDTO.setFileId(minioFile.getId());
                    minioResponseDTO.setOriginalFileName(originalFilename);
                    minioResponseDTO.setFileUrl(fileUrl);
                    MinioResponseDTOList.add(minioResponseDTO);
                }
            } catch (Exception e) {
                log.error("上传文件出错:{}", e);
                return ApiResult.error("上传文件出错");
            }
        }
        return ApiResult.success(MinioResponseDTOList);
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载文件")
    public void downloadFile(@RequestParam("fileUrl") String fileUrl, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(fileUrl)){
            response.setHeader("Content-type","text/html;charset=UTF-8");
            String msg = "文件下载失败";
            OutputStream ps = response.getOutputStream();
            ps.write(msg.getBytes(StandardCharsets.UTF_8));
            return;
        }
        try {
            //拿到文件路径
            String url = fileUrl.split("9000/")[1];
            //获取文件对象
            InputStream object = minioClientUtils.getObject(MinioConstant.FSP_DEV, url.substring(url.indexOf("/") + 1));
            byte[] bytes = new byte[1024];
            int len = 0;
            response.reset();
            response.setHeader("Content-Disposition","attachment/filename="+
                    URLEncoder.encode(url.substring(url.lastIndexOf("/")+1),"UTF-8"));
            response.setContentType("application/octet-stream");//将文件下载到浏览器
//            response.setContentType("image/jpeg");//浏览器直接展示图片
            response.setCharacterEncoding("UTF-8");
            OutputStream stream = response.getOutputStream();
            while ((len = object.read(bytes))>0){
                stream.write(bytes,0,len);
            }
            stream.close();
        }catch (Exception e){
            response.setHeader("Content-type","text/html;charset=UTF-8");
            String data = "文件下载失败";
            OutputStream ps = response.getOutputStream();
            ps.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 仅仅用于测试，是否可以正常上传文件
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/test")
    @ApiOperation(value = "测试minio文件上传")
    public ApiResult testPutObject() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("D:\\data.txt");
        boolean bs = minioClientUtils.putObject("fsp-dev", "政府通.txt", fileInputStream, "text/html");
        log.info("上传成功?" + bs);
        return ApiResult.success("上传成功");
    }
}

















