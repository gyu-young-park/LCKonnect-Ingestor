package io.github.gyu_young_park.LCKonnect_Ingestor.transformer;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.merger.LCKDataMerger;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LCKDataTransformer {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataTransformer.class);
    final private LCKDataMerger lckDataMerger;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;

    public List<LCKChampionshipModel> transform() {
        LOGGER.info("Start transform");
        List<LCKLeagueRawData> lckLeagueRawDataList = lckCrawler.crawl();

        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            LOGGER.info("crawl: {}", lckLeagueRawData.getLeague());
        }

        LOGGER.info("Start lck youtubue fetcher");
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();
        for (LCKPlayListModel lckPlayListModel : lckYoutubeModel.getLckPlayListList()) {
            LOGGER.info("lck yotubue: {}", lckPlayListModel.getPlaylistName());
        }
        // TODO change List<LCKChampionshipModel> to LCKData and change the return of transform to LCKChampionshipModel
        return lckDataMerger.merge(lckLeagueRawDataList, lckYoutubeModel);
    }
}
