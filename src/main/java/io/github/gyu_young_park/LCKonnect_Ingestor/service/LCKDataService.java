package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.LCKDataTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private LCKDataTransformer lckDataTransformer;

    public void getLCKData() {
        lckDataTransformer.transform();
    }
}
