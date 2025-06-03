package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKMatchRawDataModel;

import java.util.List;

public interface LCKCrawler {
    List<LCKMatchRawDataModel> crawl();
}
