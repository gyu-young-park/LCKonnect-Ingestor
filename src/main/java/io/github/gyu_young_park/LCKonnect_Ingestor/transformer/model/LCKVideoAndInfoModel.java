package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LCKVideoAndInfoModel {
    private String winTeam;
    private int winScore;
    private String loseTeam;
    private int loseScore;
    private String youtubeId;
    private LocalDate date;
}
