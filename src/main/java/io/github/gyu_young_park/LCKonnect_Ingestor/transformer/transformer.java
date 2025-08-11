package io.github.gyu_young_park.LCKonnect_Ingestor.transformer;

import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import org.springframework.stereotype.Component;

@Component
public class transformer {
    public LCKChampionshipModel transform() {
        return new LCKChampionshipModel();
    }
}
