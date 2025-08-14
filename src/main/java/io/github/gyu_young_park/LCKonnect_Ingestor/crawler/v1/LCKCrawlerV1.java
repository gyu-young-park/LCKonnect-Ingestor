package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class LCKCrawlerV1 implements LCKCrawler {
    private static final Logger logger = LoggerFactory.getLogger(LCKCrawlerV1.class);
    private final LCKCrawlingProperties lckCrawlingProperties;
    private final LCKLeagueCrawler lckLeagueCrawler;

    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
        this.lckLeagueCrawler = new LCKLeagueCrawler();
    }

    public List<LCKLeagueRawData> crawl() {
        logger.info("LckcrawlerV1: Start crawling...");
        List<LCKLeagueRawData> lckLeagueRawDataList = new ArrayList<>();
        try {
            for (String url: lckCrawlingProperties.getTargetMatchUrl()) {
                lckLeagueRawDataList.add(lckLeagueCrawler.crawLCKLeague(url));
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("LckcrawlerV1: crawling done");

        return lckLeagueRawDataList;
    }
}
