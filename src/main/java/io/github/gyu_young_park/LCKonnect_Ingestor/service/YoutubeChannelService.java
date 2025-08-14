package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class YoutubeChannelService {
    final LCKYoutubeFetcher lckYoutubeFetcher;

    public YoutubeChannelService(LCKYoutubeFetcher lckYoutubeFetcher) {
        this.lckYoutubeFetcher = lckYoutubeFetcher;
    }

    public LCKYoutubeModel getLCKPlayList() {
        return lckYoutubeFetcher.fetch();
    }
}
