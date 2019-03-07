package sports;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportsbettingApplication {

	@PostConstruct
	public void init() {
		System.err.println("Sports Betting Started at: " + new Date());
	}

	@PreDestroy
	public void destroy() {
		System.err.println("Sports Betting Ended at: " + new Date());
	}

	public static void main(String[] args) {
		SpringApplication.run(SportsbettingApplication.class, args);
	}

}
