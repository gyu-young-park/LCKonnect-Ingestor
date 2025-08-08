package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKVideoMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.springframework.stereotype.Component;

@Component
public class LCKPlayListFetcher {
    final private LCKYoutubeAPI lckYoutubeAPI;
    final private LCKVideoMapper lckVideoMapper;

    public LCKPlayListFetcher(LCKYoutubeAPI lckYoutubeAPI, LCKVideoMapper lckVideoMapper) {
        this.lckYoutubeAPI = lckYoutubeAPI;
        this.lckVideoMapper = lckVideoMapper;
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
                for (LCKPlayListItemListRespDTO.PlaylistVideo playlistVideo : lckPlayListItemListRespDTO.getItems()) {
                    LCKVideoModel lckVideoModel = lckVideoMapper.toModel(playlistVideo);
                    System.out.println(lckVideoModel.getTitle() + " " + lckVideoModel.getVideoId());
                }
            }
            System.out.println();
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);
    }
}
