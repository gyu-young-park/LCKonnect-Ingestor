package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.LCKDataMerger;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.v1.LCKDataMergerV1;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataService.class);
    final private LCKDataMerger lckDataMerger;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;

    public List<LCKChampionshipModel> getLCKData() {
        LOGGER.info("Start transform");
        LCKCrawlRawData lckCrawlRawData = lckCrawler.crawl();

        for (LCKLeagueRawData lckLeagueRawData : lckCrawlRawData.getLckLeagueRawDataList()) {
            LOGGER.info("crawl: {}", lckLeagueRawData.getLeague());
        }

        LOGGER.info("Start lck youtubue fetcher");
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();
        for (LCKPlayListModel lckPlayListModel : lckYoutubeModel.getLckPlayListList()) {
            LOGGER.info("lck yotubue: {}", lckPlayListModel.getPlaylistName());
        }
        return lckDataMerger.merge(lckCrawlRawData, lckYoutubeModel);
    }
}
