package io.github.gyu_young_park.LCKonnect_Ingestor;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LcKonnectIngestorApplication {
	public static void main(String[] args) {
		SpringApplication.run(LcKonnectIngestorApplication.class, args);
		Dotenv dotenv = Dotenv.load();

		String youtubeApiKey = dotenv.get("YOUTUBE_API_KEY");

		System.out.println("YOUTUBE API_KEY: " + youtubeApiKey);
	}
}
