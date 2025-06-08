package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto.LCKLeagueRawDataDTO;
import org.jsoup.Jsoup;

import java.io.IOException;

public class LCKLeagueCrawler {
    private final LCKMatchCrawler lckMatchCrawler;

    public LCKLeagueCrawler() {
        this.lckMatchCrawler = new LCKMatchCrawler();
    }

    public LCKLeagueRawDataDTO crawLCKLeague(String url) throws IOException {
        return new LCKLeagueRawDataDTO(parseLeagueName(url), lckMatchCrawler.crawLCKMatchData(url));
    }

    private String parseLeagueName(String url) throws IOException {
        return Jsoup.connect(url).get().selectFirst("h1").text();
    }
}
