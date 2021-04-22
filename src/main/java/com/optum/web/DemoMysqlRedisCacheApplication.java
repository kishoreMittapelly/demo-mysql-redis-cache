package com.optum.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.optum.web")
public class DemoMysqlRedisCacheApplication extends SpringBootServletInitializer {

    private static final Class<DemoMysqlRedisCacheApplication> applicationClass = DemoMysqlRedisCacheApplication.class;
    
	public static void main(String[] args) {
		SpringApplication.run(DemoMysqlRedisCacheApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
    
}