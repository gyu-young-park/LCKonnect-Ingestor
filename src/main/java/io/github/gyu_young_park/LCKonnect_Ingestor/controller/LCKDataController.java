package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.service.LCKDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lckdata")
@RequiredArgsConstructor
public class LCKDataController {
    final private LCKDataService lckDataService;

    @GetMapping()
    public String getLCKData() {
        lckDataService.getLCKData();
        return "test!";
    }
}
