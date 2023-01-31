package com.jsu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com/jsu/dao")
public class MinioApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinioApplication.class);
        System.out.println("SwaggerUI---"+"http:localhost:8081/swagger-ui.html");
    }

}
