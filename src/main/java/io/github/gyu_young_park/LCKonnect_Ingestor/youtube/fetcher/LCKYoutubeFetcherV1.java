package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.api.LCKYoutubeAPI;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKPlayListMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper.LCKVideoMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class LCKYoutubeFetcherV1 implements LCKYoutubeFetcher {
    final private LCKYoutubeAPI lckYoutubeAPI;
    final private LCKPlayListMapper lckPlayListMapper;
    final private LCKVideoMapper lckVideoMapper;

    @Override
    public LCKYoutubeModel fetch() {
        String pageToken = "";
        LCKYoutubeModel lckYoutubeModel = new LCKYoutubeModel();
        List<LCKPlayListModel> lckPlayListModelList = new ArrayList<>();
        do {
            LCKPlayListAPIRespDTO lckPlayListAPIRespDTO = lckYoutubeAPI.getLCKPlayList(pageToken);
            for (LCKPlayListAPIRespDTO.Item playList : lckPlayListAPIRespDTO.getItems()) {
                if (!filterPlayList(playList.getSnippet().getTitle())) continue;
                LCKPlayListModel lckPlayListModel = lckPlayListMapper.toModel(playList);
                lckPlayListModel.setLckVideoList(getLckVideos(playList.getId()));
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
        return playListTitle.endsWith("게임 하이라이트") || playListTitle.endsWith("게임하이라이트");
    }

    private List<LCKVideoModel> getLckVideos(String playListId) {
        List<LCKVideoModel> lckVideoModelList = new ArrayList<>();
        String videoPageToken = "";
        do {
            LCKPlayListItemListRespDTO lckPlayListItemListRespDTO = lckYoutubeAPI.getLCKPlaylistItemList(playListId, videoPageToken);
            lckVideoModelList.addAll(lckVideoMapper.toModelList(lckPlayListItemListRespDTO.getItems()));
            videoPageToken = lckPlayListItemListRespDTO.getNextPageToken();
        } while (videoPageToken != null);

        return removeDuplicatedData(lckVideoModelList);
    }

    private List<LCKVideoModel> removeDuplicatedData(List<LCKVideoModel> lckVideoModelList) {
        List<LCKVideoModel> retLCKVideoModelList = new ArrayList<>();
        Set<String> lckVideoDuplicatedCheckSet = new HashSet<>();
        for (LCKVideoModel lckVideoModel: lckVideoModelList) {
            if (lckVideoDuplicatedCheckSet.contains(lckVideoModel.getVideoId())) continue;
            retLCKVideoModelList.add(lckVideoModel);
            lckVideoDuplicatedCheckSet.add(lckVideoModel.getVideoId());
        }
        return retLCKVideoModelList;
    }
}
