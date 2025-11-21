package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "crawling.lck")
public class LCKCrawlingProperties {
    private List<String> targetMatchUrl;
}
