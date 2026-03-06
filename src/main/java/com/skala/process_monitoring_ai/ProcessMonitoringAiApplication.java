package com.skala.process_monitoring_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class ProcessMonitoringAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessMonitoringAiApplication.class, args);
	}

}
