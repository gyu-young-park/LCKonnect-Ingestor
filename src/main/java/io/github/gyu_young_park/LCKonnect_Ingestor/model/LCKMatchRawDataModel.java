package io.github.gyu_young_park.LCKonnect_Ingestor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LCKMatchRawDataModel {
    private String id;
    private String league;
    private Team leftTeam;
    private Team rightTeam;
    private boolean isPlayed;
    private LocalDate date;
}
