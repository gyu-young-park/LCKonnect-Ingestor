package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto.LCKLeagueRawDataDTO;

import java.util.List;

public interface LCKCrawler {
    List<LCKLeagueRawDataDTO> crawl();
}
