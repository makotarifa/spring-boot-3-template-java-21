package com.angelmorando.template.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.angelmorando.template.repository.mybatis")
public class MyBatisConfig {

}
