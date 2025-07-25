package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LCKLeagueRawData {
    private String league;
    private List<LCKMatchRawData> lckMatchRawDataList;
}
