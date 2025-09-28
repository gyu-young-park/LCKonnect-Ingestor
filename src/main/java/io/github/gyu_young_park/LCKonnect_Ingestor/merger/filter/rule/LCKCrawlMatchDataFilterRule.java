package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LCKCrawlMatchDataFilterRule<T extends LCKMatchRawData> implements LCKDataFilterRule<T>{
    final private Map<String, String> filterTargetMatchIdMap = new HashMap<>();

    public LCKCrawlMatchDataFilterRule(List<String> filterTargetMatchIdList) {
        for (String id: filterTargetMatchIdList) {
            filterTargetMatchIdMap.put(id, id);
        }
    }

    @Override
    public boolean filterByRule(T data) {
        return !filterTargetMatchIdMap.containsKey(data.getId());
    }
}
