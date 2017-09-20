package com.msa.cqrs.queryservice;

import com.msa.cqrs.queryservice.entity.AccountEntity;
import com.msa.cqrs.queryservice.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class QueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryServiceApplication.class, args);
	}

	@Configuration
	static class InitCommand implements CommandLineRunner {
		@Autowired
		private AccountRepository accountRepository;
		@Override
		public void run(String... args) throws Exception {
			accountRepository.save(new AccountEntity("test",1000,"test"));
		}
	}
}

@RefreshScope
@RestController
class MessageRestController {
	private String message = "test";

	@RequestMapping("/message")
	public String message() {
		return message;
	}
}