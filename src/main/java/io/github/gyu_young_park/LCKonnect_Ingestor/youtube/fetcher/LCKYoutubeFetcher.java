package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import org.springframework.stereotype.Component;

public interface LCKYoutubeFetcher {
    LCKYoutubeModel fetch();
}
