package com.jsu.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsu.entity.MinioFile;
import com.jsu.service.MinioFileService;
import com.jsu.vo.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

///**
// * (MinioFile)表控制层
// *
// * @author makejava
// * @since 2022-08-17 12:27:16
// */
@RestController
@RequestMapping("minioFile")
public class MinioFileController {
    /**
     * 服务对象
     */
    @Resource
    private MinioFileService minioFileService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param minioFile 查询实体
     * @return 所有数据
     */
    @GetMapping
    public ApiResult selectAll(Page<MinioFile> page, MinioFile minioFile) {
//      return ApiResult.success(this.minioFileService.page(page, new QueryWrapper<>()));
        List<MinioFile> list = this.minioFileService.list();
        return ApiResult.success(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ApiResult selectOne(@PathVariable Serializable id) {
        return ApiResult.success(this.minioFileService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param minioFile 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ApiResult insert(@RequestBody MinioFile minioFile) {
        return ApiResult.success(this.minioFileService.save(minioFile));
    }

    /**
     * 修改数据
     *
     * @param minioFile 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ApiResult update(@RequestBody MinioFile minioFile) {
        return ApiResult.success(this.minioFileService.updateById(minioFile));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ApiResult delete(@RequestParam("idList") List<Long> idList) {
        return ApiResult.success(this.minioFileService.removeByIds(idList));
    }
}

