package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;

import java.util.Set;

public class LCKVideoDataWastedVideoFilterRule<T extends LCKVideoModel> implements LCKDataFilterRule<T>{
    final private static Set<String> FILTER_LIST = Set.of("Private video", "Deleted video");

    @Override
    public boolean filterByRule(T data) {
        return !FILTER_LIST.contains(data.getTitle().trim());
    }
}
