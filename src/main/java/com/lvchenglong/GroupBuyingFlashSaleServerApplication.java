package com.lvchenglong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lvchenglong.mapper")
public class GroupBuyingFlashSaleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupBuyingFlashSaleServerApplication.class, args);
	}

}
