package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.EnvConfigManager;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.YoutubeAPIConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKChannelAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LCKYoutubeAPI {
    private final EnvConfigManager envConfigManager;
    private final YoutubeAPIConfiguration youtubeAPIConfiguration;
    private final WebClient youtubeWebClient;

    public LCKYoutubeAPI(EnvConfigManager envConfigManager,
                                 WebClient youtubeWebClient,
                                 YoutubeAPIConfiguration youtubeAPIConfiguration) {
        this.envConfigManager = envConfigManager;
        this.youtubeWebClient = youtubeWebClient;
        this.youtubeAPIConfiguration= youtubeAPIConfiguration;
    }

    public LCKPlayListAPIRespDTO getLCKPlayList() {
        return getLCKPlayList("");
    }

    public LCKPlayListAPIRespDTO getLCKPlayList(String pageToken) {
        return youtubeWebClient.get().uri(uriBuilder -> uriBuilder
                .path(youtubeAPIConfiguration.getPath().getPlaylist())
                .queryParam("part", "snippet")
                .queryParam("pageToken", pageToken)
                .queryParam("maxResults", 50)
                .queryParam("key", envConfigManager.getYoutubeAPIkey())
                .queryParam("channelId", youtubeAPIConfiguration.getChannelId()).build()).retrieve().bodyToMono(LCKPlayListAPIRespDTO.class).block();
    }

    public LCKChannelAPIRespDTO getLCkChannelData(String channelName) {
        return youtubeWebClient.get().uri(uriBuilder -> uriBuilder
                .path(youtubeAPIConfiguration.getPath().getChannel())
                .queryParam("part", "snippet")
                .queryParam("forHandle", channelName)
                .queryParam("key", envConfigManager.getYoutubeAPIkey())
                .build()).retrieve().bodyToMono(LCKChannelAPIRespDTO.class).block();
    }
}
