package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKPlayListMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKVideoMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LCKPlayListFetcher {
    final private LCKYoutubeAPI lckYoutubeAPI;
    final private LCKPlayListMapper lckPlayListMapper;

    public LCKPlayListFetcher(LCKYoutubeAPI lckYoutubeAPI, LCKPlayListMapper lckPlayListMapper) {
        this.lckYoutubeAPI = lckYoutubeAPI;
        this.lckPlayListMapper = lckPlayListMapper;
    }

    public Optional<List<LCKPlayListModel>> fetch() {
        String pageToken = "";
        List<LCKPlayListModel> lckPlayListModelList = new ArrayList<>();
        do {
            LCKPlayListAPIRespDTO lckPlayListAPIRespDTO = lckYoutubeAPI.getLCKPlayList(pageToken);
            for (LCKPlayListAPIRespDTO.Item item : lckPlayListAPIRespDTO.getItems()) {
                if (!item.getSnippet().getTitle().endsWith("게임 하이라이트")) continue;

                System.out.println("Playlist Title: " + item.getSnippet().getTitle() + " " + item.getId());
                LCKPlayListItemListRespDTO lckPlayListItemListRespDTO = lckYoutubeAPI.getLCKPlaylistItemList(item.getId());
                LCKPlayListModel lckPlayListModel = lckPlayListMapper.toModel(lckPlayListItemListRespDTO);
                lckPlayListModelList.add(lckPlayListModel);
            }
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);
        return Optional.of(lckPlayListModelList);
    }
}
