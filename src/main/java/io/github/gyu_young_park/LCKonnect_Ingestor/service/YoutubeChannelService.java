package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class YoutubeChannelService {
    final LCKYoutubeAPI lckYoutubeAPI;

    public YoutubeChannelService(LCKYoutubeAPI lckYoutubeAPI) {
        this.lckYoutubeAPI = lckYoutubeAPI;
    }

    public LCKPlayListAPIRespDTO getLCKPlayList() {
        return lckYoutubeAPI.getLCKPlayList();
    }
}
