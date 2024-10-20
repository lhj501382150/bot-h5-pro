package com.hml;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.hml.mall.mapper")
@EnableTransactionManagement
@EnableScheduling
public class MallManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallManagerApplication.class, args);
	}

}

