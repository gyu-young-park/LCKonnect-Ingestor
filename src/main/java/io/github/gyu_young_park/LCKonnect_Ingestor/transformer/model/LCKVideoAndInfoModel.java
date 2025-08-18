package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LCKVideoAndInfoModel {
    private String videoTitle;
    private String videoId;
    private LCKTeamModel winTeam;
    private LCKTeamModel loseTeam;
    private LocalDate date;
}
