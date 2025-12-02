package com.dormclean.dorm_cleaning_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DormCleaningManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DormCleaningManagementApplication.class, args);
	}

}
