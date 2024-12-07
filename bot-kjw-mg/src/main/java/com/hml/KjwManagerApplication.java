package com.hml;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hml.thread.GetDataThread;
import com.hml.thread.NiuGetDataThread;

@EnableAsync
@SpringBootApplication
@MapperScan("com.hml.mall.mapper")
@EnableTransactionManagement
@EnableScheduling
public class KjwManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KjwManagerApplication.class, args);
		
		new GetDataThread().start();
	    new NiuGetDataThread().start();
		 
	}

}

