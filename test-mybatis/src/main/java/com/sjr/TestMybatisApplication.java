package com.sjr;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan("com.sjr.model")
@ComponentScan(basePackages = {"com.sjr"})
@MapperScan(value = "com.sjr.dao")
@EnableTransactionManagement//启动事务注解
@SpringBootApplication
public class TestMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestMybatisApplication.class,args);
    }
}
