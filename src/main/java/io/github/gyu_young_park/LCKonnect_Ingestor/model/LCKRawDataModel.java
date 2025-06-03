package io.github.gyu_young_park.LCKonnect_Ingestor.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LCKRawDataModel {
    private String id;
    private String winTeam;
    private String loseTeam;
    private Integer winTeamScore;
    private Integer loseTeamScore;
    private LocalDate date;
}
