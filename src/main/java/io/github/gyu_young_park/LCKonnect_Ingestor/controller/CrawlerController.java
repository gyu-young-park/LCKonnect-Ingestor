package io.github.gyu_young_park.LCKonnect_Ingestor.controller;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawlerV1;
import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKRawDataModel;
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
    public Map<String, List<LCKRawDataModel>> getLCkRawDataModelList() {
        List<LCKRawDataModel> lckRawDataModelList = lckCrawler.crawl();
        Map<String, List<LCKRawDataModel>> data = new HashMap<>();
        data.put("message", lckRawDataModelList);
        return data;
    }
}
