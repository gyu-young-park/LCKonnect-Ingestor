package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto.LCKMatchRawDataDTO;
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

    public List<LCKMatchRawDataDTO> crawLCKMatchData(String url) throws IOException, NullPointerException {
        List<LCKMatchRawDataDTO> lckMatchRawDataDTOList = new ArrayList<>();
        Elements table = Jsoup.connect(url).get().select("table.table_list").select("tbody > tr");
        for (Element tableRow: table) {
            lckMatchRawDataDTOList.add(parseLCkMatchRawDataModelFromTableData(tableRow.select("td")));
        }
        return lckMatchRawDataDTOList;
    }

    private String parseIdFromElement(Element element) {
        return element.selectFirst("a").attr("href").split("/")[3];
    }

    private LCKMatchRawDataDTO parseLCkMatchRawDataModelFromTableData(Elements tableData) throws IOException, NullPointerException {
        LCKMatchRawDataDTO lckMatchRawDataDTO = new LCKMatchRawDataDTO();
        String matchId = parseIdFromElement(tableData.get(0));
        String[] scores = tableData.get(2).text().split(" ");
        if (scores.length >= 3) {
            lckMatchRawDataDTO.setLeftTeamTotalScore(Integer.parseInt(scores[0]));
            lckMatchRawDataDTO.setRightTeamTotalScore(Integer.parseInt(scores[2]));
            lckMatchRawDataDTO.setLckGameRawDataDTOList(lckGameCrawler.crawlLCKGameRawDataModelList(matchId));
            lckMatchRawDataDTO.setPlayed(true);
        }
        lckMatchRawDataDTO.setId(matchId);
        lckMatchRawDataDTO.setLeftTeam(tableData.get(1).text());
        lckMatchRawDataDTO.setRightTeam(tableData.get(3).text());
        lckMatchRawDataDTO.setDate(LocalDate.parse(tableData.get(6).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return lckMatchRawDataDTO;
    }
}
