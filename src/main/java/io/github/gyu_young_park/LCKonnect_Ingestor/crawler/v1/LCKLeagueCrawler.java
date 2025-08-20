package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LCKLeagueCrawler {
    final static private Logger LOGGER = LoggerFactory.getLogger(LCKLeagueCrawler.class);
    final private LCKMatchCrawler lckMatchCrawler;

    public LCKLeagueCrawler() {
        this.lckMatchCrawler = new LCKMatchCrawler();
    }

    public LCKLeagueRawData crawLCKLeague(String url) throws IOException {
        LOGGER.info("crawling: {}", url);
        return new LCKLeagueRawData(parseLeagueName(url), lckMatchCrawler.crawLCKMatchData(url));
    }

    private String parseLeagueName(String url) throws IOException {
        return Jsoup.connect(url).get().selectFirst("h1").text();
    }
}
