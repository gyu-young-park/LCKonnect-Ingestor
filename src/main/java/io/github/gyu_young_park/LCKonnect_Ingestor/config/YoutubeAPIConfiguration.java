package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Component
@ConfigurationProperties(prefix = "youtube.api")
public class YoutubeAPIConfiguration {
    private String baseUrl;
    private String channelId;
    private YoutubeAPIPath path;

    @Data
    public static class YoutubeAPIPath {
        private String channel;
        private String playlist;
    }

    @Bean
    public WebClient youtubeWebClient() {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
