package com.sms.data.csm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@MapperScan("com.sms.data.csm.mapper")
@SpringBootApplication
public class CooperativeShortMessageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooperativeShortMessageWebApplication.class, args);
    }

}
