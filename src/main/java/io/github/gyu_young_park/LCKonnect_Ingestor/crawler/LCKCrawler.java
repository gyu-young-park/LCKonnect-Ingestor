package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKLeagueRawDataModel;

import java.util.List;

public interface LCKCrawler {
    List<LCKLeagueRawDataModel> crawl();
}
