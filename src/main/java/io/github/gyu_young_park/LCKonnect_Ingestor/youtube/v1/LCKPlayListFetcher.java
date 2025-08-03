package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
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
                if (!item.getSnippet().getTitle().endsWith("게임 하이라이트")) continue;
                // TODO: api dto <-> model 간 변환 작업
                System.out.println("Playlist Title: " + item.getSnippet().getTitle() + " " + item.getId());
                LCKPlayListItemListRespDTO lckPlayListItemListRespDTO = lckYoutubeAPI.getLCKPlaylistItemList(item.getId());

            }
            System.out.println();
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);
    }
}
