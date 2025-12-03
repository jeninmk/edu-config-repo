package org.com.education.progress.progressservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories("org.com.education.progress.progressservice.repository")
@EntityScan("org.com.education.progress.progressservice.entity")
public class ProgressServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProgressServiceApplication.class);
		app.setAdditionalProfiles("dev"); // optional
		app.run(args);
	}

}
