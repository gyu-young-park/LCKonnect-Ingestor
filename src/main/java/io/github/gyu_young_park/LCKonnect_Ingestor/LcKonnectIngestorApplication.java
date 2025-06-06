package io.github.gyu_young_park.LCKonnect_Ingestor;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class LcKonnectIngestorApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(LcKonnectIngestorApplication.class, args);
		Dotenv dotenv = Dotenv.load();
		String youtubeApiKey = dotenv.get("YOUTUBE_API_KEY");
		System.out.println("YOUTUBE API_KEY: " + youtubeApiKey);

		try {
			String urlStr = String.format(
					"https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&id=%s&key=%s",
					"UCw1DsweY9b2AKGjV4kGJP1A", youtubeApiKey);

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 정상 응답
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream())
				);
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				System.out.println("API Response:");
				System.out.println(response.toString());

				// TODO: JSON 파싱해서 원하는 데이터 추출 가능 (예: Jackson, Gson 등)
			} else {
				System.err.println("API 요청 실패, 응답 코드: " + responseCode);
			}

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
