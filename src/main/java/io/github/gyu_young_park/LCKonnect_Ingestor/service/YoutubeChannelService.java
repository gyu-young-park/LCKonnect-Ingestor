package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.v1.LCKPlayListFetcher;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Data
@Component
public class YoutubeChannelService {
    final LCKPlayListFetcher lckPlayListFetcher;

    public YoutubeChannelService(LCKPlayListFetcher lckPlayListFetcher) {
        this.lckPlayListFetcher = lckPlayListFetcher;
    }

    public void getLCKPlayList() {
        List<LCKPlayListModel> lckPlayListModelList = lckPlayListFetcher.fetch().orElseThrow(() -> new NoSuchElementException("LCKPlayList fetch is failed"));
        for (LCKPlayListModel lckPlayListModel : lckPlayListModelList) {
            System.out.println("playlist: " + lckPlayListModel.getPlaylistName() + ", videos: " + lckPlayListModel.getLckVideoList().size());
        }

    }
}
