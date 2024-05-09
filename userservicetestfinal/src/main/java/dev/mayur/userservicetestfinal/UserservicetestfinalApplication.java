package dev.mayur.userservicetestfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication
public class UserservicetestfinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserservicetestfinalApplication.class, args);
	}

}
