package lk.ac.mrt.cse.spark.kusala248201v;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class LyricsGenrePredictorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LyricsGenrePredictorApplication.class, args);
	}

}
