package com.hml;

import java.util.TimeZone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hml.thread.GetDataThread;
import com.hml.thread.HkDataThread;
import com.hml.thread.JndDataThread;
import com.hml.thread.MaKaoDataThread;
import com.hml.thread.NewMaKaoDataThread;
import com.hml.thread.NiuGetDataThread;
import com.hml.utils.DateTimeUtils;

@EnableAsync
@SpringBootApplication
@MapperScan("com.hml.mall.mapper")
@EnableTransactionManagement
@EnableScheduling
public class KjwManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KjwManagerApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		System.out.println(DateTimeUtils.getDateTime());
		new GetDataThread().start();
	    new NiuGetDataThread().start();
		 new MaKaoDataThread().start();
		new NewMaKaoDataThread().start();
		new HkDataThread().start();
		new JndDataThread().start();
	}

}

