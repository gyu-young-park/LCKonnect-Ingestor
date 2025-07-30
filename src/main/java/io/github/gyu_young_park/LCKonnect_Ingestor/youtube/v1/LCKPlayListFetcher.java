package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import org.springframework.stereotype.Component;

@Component
public class LCKPlayListFetcher {
    final private LCKYoutubeAPI lckYoutubeAPI;

    public LCKPlayListFetcher(LCKYoutubeAPI lckYoutubeAPI) {
        this.lckYoutubeAPI = lckYoutubeAPI;
    }

    public void fetch() {
        String pageToken = "";
        do {
            LCKPlayListAPIRespDTO lckPlayListAPIRespDTO = lckYoutubeAPI.getLCKPlayList(pageToken);
            for (LCKPlayListAPIRespDTO.Item item : lckPlayListAPIRespDTO.getItems()) {
                System.out.println(item.getSnippet().getTitle());
            }
            System.out.println();
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);
    }
}
