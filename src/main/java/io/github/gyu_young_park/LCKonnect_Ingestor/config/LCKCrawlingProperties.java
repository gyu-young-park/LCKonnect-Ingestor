package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "crawling.lck")
@Data
public class LCKCrawlingProperties {
    private List<String> targetMatchUrl;
}
