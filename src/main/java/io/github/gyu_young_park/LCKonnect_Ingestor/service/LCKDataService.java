package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.LCKDataTransformer;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private LCKDataTransformer lckDataTransformer;

    public List<LCKChampionshipModel> getLCKData() {
        return lckDataTransformer.transform();
    }
}
