package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;

import java.util.List;

public interface LCKCrawler {
    List<LCKLeagueRawData> crawl();
}
