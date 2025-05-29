package io.github.gyu_young_park.LCKonnect_Ingestor.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Hello World");
        return data;
    }
}
