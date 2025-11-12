package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LCKVideoDataFilterRule<T extends LCKVideoModel> implements LCKDataFilterRule<T>{
    Map<String, Boolean> filterVideoMap = new HashMap<>();
    public LCKVideoDataFilterRule(List<String> filterVideoList) {
        for (String videoId: filterVideoList) {
            filterVideoMap.put(videoId, true);
        }
    }

    @Override
    public boolean filterByRule(T data) {return !filterVideoMap.containsKey(data.getVideoId());
    }
}
