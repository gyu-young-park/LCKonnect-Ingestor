package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import io.github.gyu_young_park.LCKonnect_Ingestor.config.LCKCrawlingProperties;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class LCKCrawlerV1 {
    private final LCKCrawlingProperties lckCrawlingProperties;
    public LCKCrawlerV1(LCKCrawlingProperties lckCrawlingProperties) {
        this.lckCrawlingProperties = lckCrawlingProperties;
    }

    public void crawl() {
        try {
            for (String url : lckCrawlingProperties.getTargetUrl()) {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("table.table_list").select("tbody > tr");
                for (Element row: elements) {
                    Elements cols = row.select("td");
                    String leftTeam = cols.get(1).text();
                    String href = cols.get(0).selectFirst("a").attr("href");
                    String id = href.split("/")[3];
                    String score = cols.get(2).text();
                    String rightTeam = cols.get(3).text();
                    String date = cols.get(6).text();
                    System.out.println(id + " "+ leftTeam + " " + score  + " " + rightTeam + " | " + date); // Hanwha Life eSports 0 - 2 Gen.G eSports | 2025-04-02
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
