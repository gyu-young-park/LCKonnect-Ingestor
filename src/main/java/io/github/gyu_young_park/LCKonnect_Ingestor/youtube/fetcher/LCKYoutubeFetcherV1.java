package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKPlayListMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKVideoMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Profile("v1")
public class LCKYoutubeFetcherV1 implements LCKYoutubeFetcher {
    final private LCKYoutubeAPI lckYoutubeAPI;
    final private LCKPlayListMapper lckPlayListMapper;
    final private LCKVideoMapper lckVideoMapper;

    public LCKYoutubeFetcherV1(LCKYoutubeAPI lckYoutubeAPI,
                               LCKPlayListMapper lckPlayListMapper,
                               LCKVideoMapper lckVideoMapper) {
        this.lckYoutubeAPI = lckYoutubeAPI;
        this.lckPlayListMapper = lckPlayListMapper;
        this.lckVideoMapper = lckVideoMapper;
    }

    @Override
    public LCKYoutubeModel fetch() {
        String pageToken = "";
        LCKYoutubeModel lckYoutubeModel = new LCKYoutubeModel();
        List<LCKPlayListModel> lckPlayListModelList = new ArrayList<>();
        do {
            LCKPlayListAPIRespDTO lckPlayListAPIRespDTO = lckYoutubeAPI.getLCKPlayList(pageToken);
            for (LCKPlayListAPIRespDTO.Item item : lckPlayListAPIRespDTO.getItems()) {
                if (filterPlayList(item.getSnippet().getTitle())) continue;
                System.out.println("Playlist Title: " + item.getSnippet().getTitle() + " " + item.getId());

                List<LCKVideoModel> lckVideoModelList = new ArrayList<>();
                String videoPageToken = "";
                do {
                    LCKPlayListItemListRespDTO lckPlayListItemListRespDTO = lckYoutubeAPI.getLCKPlaylistItemList(item.getId(), videoPageToken);
                    lckVideoModelList.addAll(lckVideoMapper.toModelList(lckPlayListItemListRespDTO.getItems()));
                    videoPageToken = lckPlayListItemListRespDTO.getNextPageToken();
                } while (videoPageToken != null);
                LCKPlayListModel lckPlayListModel = new LCKPlayListModel(item.getSnippet().getTitle(),item.getId(), lckVideoModelList);
                lckPlayListModelList.add(lckPlayListModel);
            }
            pageToken = lckPlayListAPIRespDTO.getNextPageToken();
        }while (pageToken != null);

        if(lckPlayListModelList.isEmpty()) {
            throw new NoSuchElementException("Failed to fetch lck playlist");
        }

        lckYoutubeModel.setLckPlayListList(lckPlayListModelList);
        return lckYoutubeModel;
    }

    private boolean filterPlayList(String playListTitle) {
        return !playListTitle.endsWith("게임 하이라이트");
    }
}
