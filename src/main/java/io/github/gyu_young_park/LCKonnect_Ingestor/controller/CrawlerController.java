package io.github.gyu_young_park.LCKonnect_Ingestor.controller;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class CrawlerController {
    final private LCKCrawler lckCrawler;

    @GetMapping()
    public Map<String, List<LCKLeagueRawData>> getLCkRawDataModelList() {
        List<LCKLeagueRawData> lckLeagueRawDataList = lckCrawler.crawl();
        Map<String, List<LCKLeagueRawData>> data = new HashMap<>();
        data.put("message", lckLeagueRawDataList);
        return data;
    }
}
