package io.github.gyu_young_park.LCKonnect_Ingestor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class LCKMatchRawDataModel {
    private String id;
    private String leftTeam;
    private int leftTeamTotalScore;
    private String rightTeam;
    private int rightTeamTotalScore;
    private boolean isPlayed;
    private LocalDate date;
    private List<LCKGameRawDataModel> lckGameRawDataModelList;
}
