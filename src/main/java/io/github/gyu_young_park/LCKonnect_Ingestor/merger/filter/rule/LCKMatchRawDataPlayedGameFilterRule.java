package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;

public class LCKMatchRawDataPlayedGameFilterRule<T extends LCKMatchRawData> implements LCKDataFilterRule<T> {
    @Override
    public boolean filterByRule(T data) {
        return data.isPlayed();
    }
}
