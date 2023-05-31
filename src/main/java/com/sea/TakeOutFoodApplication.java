package com.sea;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 刘海洋
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.sea.mapper")
/**
 * filter配置
 * */
@ServletComponentScan
@EnableTransactionManagement
public class TakeOutFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeOutFoodApplication.class, args);
    }

}
