package io.github.gyu_young_park.LCKonnect_Ingestor.controller;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto.LCKLeagueRawDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    private LCKCrawler lckCrawler;

    @GetMapping()
    public Map<String, List<LCKLeagueRawDataDTO>> getLCkRawDataModelList() {
        List<LCKLeagueRawDataDTO> lckLeagueRawDataDTOList = lckCrawler.crawl();
        Map<String, List<LCKLeagueRawDataDTO>> data = new HashMap<>();
        data.put("message", lckLeagueRawDataDTOList);
        return data;
    }
}
