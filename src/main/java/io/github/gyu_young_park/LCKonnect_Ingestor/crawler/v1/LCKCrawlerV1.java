package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKLeagueRawDataModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKMatchRawDataModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class LCKCrawlerV1 implements LCKCrawler {
    private final LCKCrawlingProperties lckCrawlingProperties;
    private final LCKMatchCrawler lckMatchCrawler;

    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
        this.lckMatchCrawler = new LCKMatchCrawler();
    }

    public List<LCKLeagueRawDataModel> crawl() {
        List<LCKLeagueRawDataModel> lckLeagueRawDataModelList = new ArrayList<>();
        try {
            for (String url : lckCrawlingProperties.getTargetUrl()) {
                lckLeagueRawDataModelList.add(new LCKLeagueRawDataModel(url, lckMatchCrawler.crawLCKMatchData(url)
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lckLeagueRawDataModelList;
    }
}
