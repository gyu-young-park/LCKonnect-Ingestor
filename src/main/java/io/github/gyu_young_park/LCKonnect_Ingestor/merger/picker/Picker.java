package io.github.gyu_young_park.LCKonnect_Ingestor.merger.picker;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Picker {
    public static <T extends LCKMatchRawData> List<T> pickUpMatch(List<String> pickIds, List<T> lckMatchRawDataList) {
        if (pickIds == null || pickIds.isEmpty()) {
            return lckMatchRawDataList;
        }

        Map<String, Boolean> pickIdsMap = new HashMap<>();
        for (String pickId: pickIds) {
            pickIdsMap.put(pickId, true);
        }

        List<T> pickDataList = new ArrayList<>();
        for (T lckMatchRawData : lckMatchRawDataList) {
            if (pickIdsMap.containsKey(lckMatchRawData.getId())) {
                pickDataList.add(lckMatchRawData);
            }
        }
        return pickDataList;
    }
}
