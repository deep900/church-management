package com.church.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.church.data.MongoDBConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages={"com.church"})
public class ChurchManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChurchManagementApplication.class, args);
		log.info("Starting the application ..");
	}

}
