package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.concurrent.ConcurrentManager;
import io.github.gyu_young_park.LCKonnect_Ingestor.concurrent.Task;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Primary
public class LCKCrawlerV1 implements LCKCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LCKCrawlerV1.class);
    private final LCKCrawlingProperties lckCrawlingProperties;
    private final LCKLeagueCrawler lckLeagueCrawler;

    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
        this.lckLeagueCrawler = new LCKLeagueCrawler();
    }

    public LCKCrawlRawData crawl() {
        LOGGER.info("LckcrawlerV1: Start crawling...");
        List<LCKLeagueRawData> lckLeagueRawDataList = new ArrayList<>();
        try(ConcurrentManager<LCKLeagueRawData> concurrentManager = new ConcurrentManager<>(getClass().getName() + " Concurrent manager", lckCrawlingProperties.getTargetMatchUrl().size())) {
            for (String url: lckCrawlingProperties.getTargetMatchUrl()) {
                concurrentManager.submitTask(new Task<>("task " + url,
                        new Callable<LCKLeagueRawData>() {
                            @Override
                            public LCKLeagueRawData call() throws Exception {
                                return lckLeagueCrawler.crawLCKLeague(url);
                            }}, 5, TimeUnit.MINUTES));
            }
            lckLeagueRawDataList = concurrentManager.execute();
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            LOGGER.error("failed to call lck data crawl");
            throw new RuntimeException(e);
        }

        LOGGER.info("LckcrawlerV1: crawling done");
        return new LCKCrawlRawData(lckLeagueRawDataList);
    }
}
