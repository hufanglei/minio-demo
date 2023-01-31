package com.jsu.vo;


import lombok.Data;

@Data
public class MinioResponseDTO {

    private Long fileId;

    private String fileUrl;

    private String originalFileName;
}

