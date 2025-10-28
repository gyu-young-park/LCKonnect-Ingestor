package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKGameRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import org.springframework.stereotype.Component;

@Component
public class LCKGameRawDataFilter<T extends LCKGameRawData> extends LCKDataFilter<T> {
}
