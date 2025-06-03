package io.github.gyu_young_park.LCKonnect_Ingestor.controller;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawlerV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    private LCKCrawlerV1 lckCrawlerV1;

    @GetMapping()
    public Map<String, String> test() {
        lckCrawlerV1.crawl();
        Map<String, String> data = new HashMap<>();
        data.put("message", "Hello World");
        return data;
    }
}
