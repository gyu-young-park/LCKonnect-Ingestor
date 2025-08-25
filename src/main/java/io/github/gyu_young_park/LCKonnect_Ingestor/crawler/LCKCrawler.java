package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;

public interface LCKCrawler {
    LCKCrawlRawData crawl();
}
