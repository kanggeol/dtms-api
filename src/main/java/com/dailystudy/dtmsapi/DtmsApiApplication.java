package com.dailystudy.dtmsapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dailystudy.dtmsapi.mapper")
@SpringBootApplication
public class DtmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DtmsApiApplication.class, args);
	}

}
