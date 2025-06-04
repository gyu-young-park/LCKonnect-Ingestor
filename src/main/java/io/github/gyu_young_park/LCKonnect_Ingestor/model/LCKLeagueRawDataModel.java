package io.github.gyu_young_park.LCKonnect_Ingestor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LCKLeagueRawDataModel {
    private String league;
    private List<LCKMatchRawDataModel> lckMatchRawDataModelList;
}
