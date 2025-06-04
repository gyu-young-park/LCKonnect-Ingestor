package io.github.gyu_young_park.LCKonnect_Ingestor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LCKGameRawDataModel {
    private String id;
    private int gameRound;
    private String leftTeam;
    private int leftTeamScore;
    private List<String> leftTeamPicks;
    private String rightTeam;
    private int rightTeamScore;
    private List<String> rightTeamPicks;
    private LocalTime gameTime;
}
