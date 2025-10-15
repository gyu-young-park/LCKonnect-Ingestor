package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LCKMatchCrawler {
    final static private Logger LOGGER = LoggerFactory.getLogger(LCKMatchCrawler.class);
    private final LCKGameCrawler lckGameCrawler;

    public LCKMatchCrawler() {
        lckGameCrawler = new LCKGameCrawler();
    }

    public List<LCKMatchRawData> crawLCKMatchData(String url) throws IOException, NullPointerException {
        List<LCKMatchRawData> lckMatchRawDataList = new ArrayList<>();
        Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
        for (Element tableRow: table) {
            Optional<LCKMatchRawData> optionalLCKMatchRawData = parseLCkMatchRawDataModelFromTableData(tableRow.select("td"));
            if (optionalLCKMatchRawData.isEmpty()) {
                LOGGER.info("[LCKMatchCrawler]Skip to crawl the match: {}", tableRow.text());
                continue;
            }
            lckMatchRawDataList.add(optionalLCKMatchRawData.orElseThrow(() -> new NoSuchElementException(
                    "failed to crawl LCKMatchRawlData: " + tableRow.text()
            )));
        }
        return lckMatchRawDataList;
    }

    private Optional<String> parseIdFromElement(Element element) {
        Element matchHrefTag = element.selectFirst("a");
        if (matchHrefTag == null) {
            return Optional.empty();
        }
        return Optional.of(matchHrefTag.attr("href").split("/")[3]);
    }

    private Optional<LCKMatchRawData> parseLCkMatchRawDataModelFromTableData(Elements tableData) throws IOException, NullPointerException {
        LCKMatchRawData lckMatchRawData = new LCKMatchRawData();
        Optional<String> optionalMatchId = parseIdFromElement(tableData.getFirst());
        if (optionalMatchId.isEmpty()) {
            return Optional.empty();
        }
        String matchId = optionalMatchId.orElseThrow(() -> new NoSuchElementException("failed to get match id during crawling match: " + tableData.text()));
        String[] scores = tableData.get(2).text().split(" ");
        if (scores.length >= 3) {
            lckMatchRawData.setLeftTeamTotalScore(Integer.parseInt(scores[0]));
            lckMatchRawData.setRightTeamTotalScore(Integer.parseInt(scores[2]));
            lckMatchRawData.setLckGameRawDataList(lckGameCrawler.crawlLCKGameRawDataModelList(matchId));
            lckMatchRawData.setPlayed(true);
        }
        lckMatchRawData.setId(matchId);
        lckMatchRawData.setLeftTeam(tableData.get(1).text());
        lckMatchRawData.setRightTeam(tableData.get(3).text());
        lckMatchRawData.setDate(LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return Optional.of(lckMatchRawData);
    }
}
