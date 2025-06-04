package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKMatchRawDataModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LCKMatchCrawler {
    public List<LCKMatchRawDataModel> crawLCKMatchData(String url) {
        List<LCKMatchRawDataModel> lckMatchRawDataModelList = new ArrayList<>();
        try {
            String league = Jsoup.connect(url).get().selectFirst("h1").text();
            Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
            for (Element tableRow: table) {
                lckMatchRawDataModelList.add(parseLCkRawDataModelFromTableData(tableRow.select("td"), league));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return lckMatchRawDataModelList;
    }

    private String getIdFromElement(Element element) {
        String id = "";
        try {
            String href = element.selectFirst("a").attr("href");
            id = href.split("/")[3];
        } catch (NullPointerException e) {
            // TODO: SET LOGGER
            System.out.println(e);
        }
        return id;
    }

    private LCKMatchRawDataModel parseLCkRawDataModelFromTableData(Elements tableData, String league) {
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
                getIdFromElement(tableData.get(0)),
                league,
                tableData.get(1).text(),
                leftScore,
                tableData.get(3).text(),
                rightScore,
                isPlayed,
                LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                new ArrayList<>());
    }
}
