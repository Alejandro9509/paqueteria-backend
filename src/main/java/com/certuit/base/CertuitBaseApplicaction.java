package com.certuit.base;

import com.certuit.base.util.Cron;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CertuitBaseApplicaction extends SpringBootServletInitializer {

	public static void main(String[] args) {
		Cron cron = new Cron();
		SpringApplication.run(CertuitBaseApplicaction.class, args);
		cron.async();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CertuitBaseApplicaction.class);
	}
}
