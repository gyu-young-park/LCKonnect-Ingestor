package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKMatchRawDataModel;
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

    public List<LCKMatchRawDataModel> crawLCKMatchData(String url) throws IOException {
        List<LCKMatchRawDataModel> lckMatchRawDataModelList = new ArrayList<>();
        Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
        for (Element tableRow: table) {
            lckMatchRawDataModelList.add(parseLCkMatchRawDataModelFromTableData(tableRow.select("td")));
        }
        return lckMatchRawDataModelList;
    }

    private String parseIdFromElement(Element element) {
        return element.selectFirst("a").attr("href").split("/")[3];
    }

    private LCKMatchRawDataModel parseLCkMatchRawDataModelFromTableData(Elements tableData) throws IOException {
        String matchId = parseIdFromElement(tableData.get(0));
        boolean isPlayed = false;
        int leftScore = 0;
        int rightScore = 0;

        String[] scores = tableData.get(2).text().split(" ");
        if (scores.length >= 3) {
            leftScore = Integer.parseInt(scores[0]);
            rightScore = Integer.parseInt(scores[2]);
            isPlayed = true;
        }

        return new LCKMatchRawDataModel(
                matchId,
                tableData.get(1).text(),
                leftScore,
                tableData.get(3).text(),
                rightScore,
                isPlayed,
                LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                lckGameCrawler.crawlLCKGameRawDataModelList(matchId));
    }
}
