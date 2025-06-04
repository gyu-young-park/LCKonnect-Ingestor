package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKLeagueRawDataModel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class LCKCrawlerV1 implements LCKCrawler {
    private final LCKCrawlingProperties lckCrawlingProperties;
    private final LCKLeagueCrawler lckLeagueCrawler;

    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
        this.lckLeagueCrawler = new LCKLeagueCrawler();
    }

    public List<LCKLeagueRawDataModel> crawl() {
        List<LCKLeagueRawDataModel> lckLeagueRawDataModelList = new ArrayList<>();
        try {
            for (String url: lckCrawlingProperties.getTargetMatchUrl()) {
                lckLeagueRawDataModelList.add(lckLeagueCrawler.crawLCKLeague(url));
            }
        } catch (NullPointerException | IOException e) {
            // TODO: set up LOGGER and process exception processing
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return lckLeagueRawDataModelList;
    }
}
