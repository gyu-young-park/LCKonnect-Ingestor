package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import org.jsoup.Jsoup;

import java.io.IOException;

public class LCKLeagueCrawler {
    private final LCKMatchCrawler lckMatchCrawler;

    public LCKLeagueCrawler() {
        this.lckMatchCrawler = new LCKMatchCrawler();
    }

    public LCKLeagueRawData crawLCKLeague(String url) throws IOException {
        System.out.println("crawling: " + url);
        return new LCKLeagueRawData(parseLeagueName(url), lckMatchCrawler.crawLCKMatchData(url));
    }

    private String parseLeagueName(String url) throws IOException {
        return Jsoup.connect(url).get().selectFirst("h1").text();
    }
}
