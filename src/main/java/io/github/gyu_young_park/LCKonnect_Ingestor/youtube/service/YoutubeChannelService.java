package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.EnvConfigManager;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.YoutubeAPIConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.ChannelResponseDTO;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Data
@Component
public class YoutubeChannelService {
    private final String channelApiUrl = "https://www.googleapis.com/youtube/v3/channels";
    private final EnvConfigManager envConfigManager;
    private final YoutubeAPIConfiguration youtubeAPIConfiguration;
    private final WebClient webClient;

    public YoutubeChannelService(EnvConfigManager envConfigManager,
                                 WebClient webClient,
                                 YoutubeAPIConfiguration youtubeAPIConfiguration) {
        this.envConfigManager = envConfigManager;
        this.webClient = webClient;
        this.youtubeAPIConfiguration= youtubeAPIConfiguration;
    }

    public Mono<ChannelResponseDTO> getChannelData(String channelName) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(youtubeAPIConfiguration.getPath().getChannel())
                .queryParam("part", "snippet")
                .queryParam("forHandle", channelName)
                .queryParam("key", envConfigManager.getYoutubeAPIkey())
                .build()).retrieve().bodyToMono(ChannelResponseDTO.class);
    }
}
