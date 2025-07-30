package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1.LCKPlayListFetcher;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class YoutubeChannelService {
    final LCKPlayListFetcher lckPlayListFetcher;

    public YoutubeChannelService(LCKPlayListFetcher lckPlayListFetcher) {
        this.lckPlayListFetcher = lckPlayListFetcher;
    }

    public void getLCKPlayList() {
        lckPlayListFetcher.fetch();
    }
}
