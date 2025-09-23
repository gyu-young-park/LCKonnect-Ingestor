package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LCKCrawlDataFilter<T extends LCKMatchRawData> implements LCKDataFilter<T> {
    @Override
    public List<T> filter(List<T> data) {
        return data.stream().filter(LCKMatchRawData::isPlayed).toList();
    }
}
