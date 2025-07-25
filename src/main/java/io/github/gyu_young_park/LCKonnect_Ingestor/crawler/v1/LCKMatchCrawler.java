package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LCKMatchCrawler {
    private final LCKGameCrawler lckGameCrawler;

    public LCKMatchCrawler() {
        lckGameCrawler = new LCKGameCrawler();
    }

    public List<LCKMatchRawData> crawLCKMatchData(String url) throws IOException, NullPointerException {
        List<LCKMatchRawData> lckMatchRawDataList = new ArrayList<>();
        Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
        for (Element tableRow: table) {
            lckMatchRawDataList.add(parseLCkMatchRawDataModelFromTableData(tableRow.select("td")));
        }
        return lckMatchRawDataList;
    }

    private String parseIdFromElement(Element element) {
        return element.selectFirst("a").attr("href").split("/")[3];
    }

    private LCKMatchRawData parseLCkMatchRawDataModelFromTableData(Elements tableData) throws IOException, NullPointerException {
        LCKMatchRawData lckMatchRawData = new LCKMatchRawData();
        String matchId = parseIdFromElement(tableData.get(0));
        String[] scores = tableData.get(2).text().split(" ");
        if (scores.length >= 3) {
            lckMatchRawData.setLeftTeamTotalScore(Integer.parseInt(scores[0]));
            lckMatchRawData.setRightTeamTotalScore(Integer.parseInt(scores[2]));
            lckMatchRawData.setLckGameRawDataDTOList(lckGameCrawler.crawlLCKGameRawDataModelList(matchId));
            lckMatchRawData.setPlayed(true);
        }
        lckMatchRawData.setId(matchId);
        lckMatchRawData.setLeftTeam(tableData.get(1).text());
        lckMatchRawData.setRightTeam(tableData.get(3).text());
        lckMatchRawData.setDate(LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return lckMatchRawData;
    }
}
