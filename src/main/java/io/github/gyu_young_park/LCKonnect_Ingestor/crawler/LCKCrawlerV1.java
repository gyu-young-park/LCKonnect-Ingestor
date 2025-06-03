package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKMatchRawDataModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.Team;
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

    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
    }

    public List<LCKMatchRawDataModel> crawl() {
        List<LCKMatchRawDataModel> lckRawDataModelList = new ArrayList<>();
        try {
            for (String url : lckCrawlingProperties.getTargetUrl()) {
                String league = Jsoup.connect(url).get().selectFirst("h1").text();
                Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
                for (Element tableRow: table) {
                    LCKMatchRawDataModel model = parseLCkRawDataModelFromTableData(tableRow.select("td"), league);
                    lckRawDataModelList.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lckRawDataModelList;
    }

    private String getIdFromElement(Element element) {
        String id = "";
        try {
            String href = element.selectFirst("a").attr("href");
            id = href.split("/")[3];
        } catch (NullPointerException nullPointerException) {
            // TODO: SET LOGGER
            System.out.println(nullPointerException);
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
                new Team(tableData.get(1).text(), leftScore),
                new Team(tableData.get(3).text(), rightScore),
                isPlayed,
                LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
