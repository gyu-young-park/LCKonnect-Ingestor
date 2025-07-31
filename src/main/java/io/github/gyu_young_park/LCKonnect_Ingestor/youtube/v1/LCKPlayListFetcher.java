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
                System.out.println(item.getSnippet().getTitle() + " " + item.getId());
                LCKPlayListItemListRespDTO lckPlayListItemListRespDTO = lckYoutubeAPI.getLCKPlaylistItemList(item.getId());
                for (LCKPlayListItemListRespDTO.PlaylistItem playlistItem : lckPlayListItemListRespDTO.getItems()) {
                    System.out.println("Title:" + playlistItem.getSnippet().getTitle() + ", video id:" + playlistItem.getId());
                }
            }
            System.out.println();
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);
    }
}
