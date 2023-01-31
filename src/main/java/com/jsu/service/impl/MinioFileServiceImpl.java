package com.jsu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsu.dao.MinioFileDao;
import com.jsu.entity.MinioFile;
import com.jsu.service.MinioFileService;
import org.springframework.stereotype.Service;

/**
 * (MinioFile)表服务实现类
 *
 * @author makejava
 * @since 2022-08-17 12:27:47
 */
@Service
public class MinioFileServiceImpl extends ServiceImpl<MinioFileDao, MinioFile> implements MinioFileService {

}

