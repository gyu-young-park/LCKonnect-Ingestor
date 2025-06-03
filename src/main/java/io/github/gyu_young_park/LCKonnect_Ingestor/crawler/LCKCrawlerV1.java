package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKRawDataModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.Team;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public List<LCKRawDataModel> crawl() {
        List<LCKRawDataModel> lckRawDataModelList = new ArrayList<>();
        try {
            for (String url : lckCrawlingProperties.getTargetUrl()) {
                Document doc = Jsoup.connect(url).get();
                System.out.println("url " + url);
                Elements elements = doc.select("table.table_list").select("tbody > tr");
                for (Element row: elements) {
                    Elements columns = row.select("td");
                    LCKRawDataModel model = parseLCkRawDataModelFromElements(columns);
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

    private LCKRawDataModel parseLCkRawDataModelFromElements(Elements columns) {
        boolean isPlayed = false;
        String leftScore = "0";
        String rightScore = "0";
        String[] scores = columns.get(2).text().split(" ");
        if (scores.length >= 3) {
            leftScore = scores[0];
            rightScore = scores[2];
            isPlayed = true;
        }

        LocalDate date = LocalDate.parse(columns.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new LCKRawDataModel(getIdFromElement(columns.get(0)),
                new Team(columns.get(1).text(), Integer.parseInt(leftScore)),
                new Team(columns.get(3).text(), Integer.parseInt(rightScore)),
                isPlayed, date);
    }
}
