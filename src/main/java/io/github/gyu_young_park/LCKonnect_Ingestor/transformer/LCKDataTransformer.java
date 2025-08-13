package io.github.gyu_young_park.LCKonnect_Ingestor.transformer;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LCKDataTransformer {
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;

    public LCKChampionshipModel transform() {
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();
        List<LCKLeagueRawData> lckLeagueRawDataList = lckCrawler.crawl();

        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            System.out.println("crawl: " + lckLeagueRawData.getLeague());
        }

        System.out.println("------------------------Done-------------------");

        for (LCKPlayListModel lckPlayListModel : lckYoutubeModel.getLckPlayListList()) {
            System.out.println("lck yotubue: " + lckPlayListModel.getPlaylistName());
        }


        return new LCKChampionshipModel();
    }
}
