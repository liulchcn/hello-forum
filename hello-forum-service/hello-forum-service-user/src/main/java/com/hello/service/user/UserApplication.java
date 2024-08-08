package com.hello.service.user;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author L1835
 */
@SpringBootApplication
@MapperScan("com.hello.service.user.mapper")
public class UserApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	@Bean
	public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List ddlList) {
		return new DdlApplicationRunner(ddlList);
	}

}
