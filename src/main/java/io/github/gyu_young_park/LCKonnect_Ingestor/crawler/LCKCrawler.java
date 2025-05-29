package io.github.gyu_young_park.LCKonnect_Ingestor.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LCKCrawler {
    static final String LCK_2025_SPRING_LEAGUE_URL= "https://gol.gg/tournament/tournament-matchlist/LCK%202025%20Rounds%201-2/";
    public void crawl() {
        try {
            Document doc = Jsoup.connect(LCKCrawler.LCK_2025_SPRING_LEAGUE_URL).get();
            Elements elements = doc.select("table.table_list").select("tbody > tr");
            for (Element row: elements) {
                Elements cols = row.select("td");
                String leftTeam = cols.get(1).text();
                String score = cols.get(2).text();
                String rightTeam = cols.get(3).text();
                String date = cols.get(6).text();
                System.out.println(leftTeam + " " + score  + " " + rightTeam + " | " + date); // Hanwha Life eSports 0 - 2 Gen.G eSports | 2025-04-02
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
