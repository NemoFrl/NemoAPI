package nemofrl.nemoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"nemofrl.nemoapi"})
@ImportResource("classpath:applicationContext.xml")
@EnableScheduling
public class NemoAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(NemoAPIApplication.class, args);
	}

}
