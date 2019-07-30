package nemofrl.pixiv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"nemofrl.pixiv"})
public class PixivApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixivApplication.class, args);
	}

}
