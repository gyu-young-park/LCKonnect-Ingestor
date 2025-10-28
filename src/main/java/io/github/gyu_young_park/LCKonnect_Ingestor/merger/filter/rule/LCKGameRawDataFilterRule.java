package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKGameRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LCKGameRawDataFilterRule<T extends LCKGameRawData> implements LCKDataFilterRule<T>{
    final private Map<String, String> filterTargetGameIdMap = new HashMap<>();

    public LCKGameRawDataFilterRule(List<String> filterTargetGameIdList) {
        for (String id: filterTargetGameIdList) {
            filterTargetGameIdMap.put(id, id);
        }
    }

    @Override
    public boolean filterByRule(T data) {
        return !filterTargetGameIdMap.containsKey(data.getId());
    }
}
