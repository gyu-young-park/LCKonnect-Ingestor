package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.service.LCKDataService;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lckdata")
@RequiredArgsConstructor
public class LCKDataController {
    final private LCKDataService lckDataService;

    @GetMapping()
    public List<LCKChampionshipModel> getLCKData() {
        return lckDataService.getLCKData();
    }
}
