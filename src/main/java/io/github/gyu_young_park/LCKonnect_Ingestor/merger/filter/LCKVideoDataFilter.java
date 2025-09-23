package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class LCKVideoDataFilter<T extends LCKVideoModel> implements LCKDataFilter<T> {
    final private static Set<String> FILTER_LIST = Set.of("Private video", "Deleted video");

    @Override
    public List<T> filter(List<T> data) {
        return data.stream().filter(video -> !FILTER_LIST.contains(video.getTitle().trim())).toList();
    }
}
