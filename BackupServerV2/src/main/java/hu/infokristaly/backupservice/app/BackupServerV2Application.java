package hu.infokristaly.backupservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "hu.infokristaly.backupservice.repository" })
@EntityScan(basePackages = { "hu.infokristaly.backupservice.model" })
@ComponentScan(basePackages = { "hu.infokristaly.backupservice.controller", "hu.infokristaly.backupservice.service" })
public class BackupServerV2Application {

	public static void main(String[] args) {
		SpringApplication.run(BackupServerV2Application.class, args);
	}

}
