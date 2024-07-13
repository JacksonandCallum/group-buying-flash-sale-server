package com.lvchenglong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.lvchenglong.mapper")
@EnableScheduling
public class GroupBuyingFlashSaleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupBuyingFlashSaleServerApplication.class, args);
	}

}
