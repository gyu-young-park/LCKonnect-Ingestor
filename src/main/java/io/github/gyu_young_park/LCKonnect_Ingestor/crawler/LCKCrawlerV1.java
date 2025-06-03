package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKRawDataModel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                Elements elements = doc.select("table.table_list").select("tbody > tr");
                for (Element row: elements) {
                    LCKRawDataModel lckRawDataModel = new LCKRawDataModel();
                    Elements cols = row.select("td");
                    lckRawDataModel.setId(getIdFromElement(cols.get(0)));

                    String leftTeam = cols.get(1).text();
                    String score = cols.get(2).text();
                    String rightTeam = cols.get(3).text();
                    String date = cols.get(6).text();
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
}
