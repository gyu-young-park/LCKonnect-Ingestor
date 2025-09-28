package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.springframework.stereotype.Component;

@Component
public class LCKVideoDataFilter<T extends LCKVideoModel> extends LCKDataFilter<T> {
}
