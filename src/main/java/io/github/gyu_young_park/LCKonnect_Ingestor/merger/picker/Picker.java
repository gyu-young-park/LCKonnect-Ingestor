package io.github.gyu_young_park.LCKonnect_Ingestor.merger.picker;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Picker {
    public static <T extends LCKMatchRawData> List<T> pickUpMatch(List<String> pickIds, Map<String, T> dataMap) {
        if (pickIds == null || pickIds.isEmpty()) {
            return new ArrayList<>(dataMap.values());
        }

        List<T> pickDataList = new ArrayList<>();
        for (String id: pickIds) {
            if (dataMap.containsKey(id)) {
                pickDataList.add(dataMap.get(id));
            }
        }
        return pickDataList;
    }
}
