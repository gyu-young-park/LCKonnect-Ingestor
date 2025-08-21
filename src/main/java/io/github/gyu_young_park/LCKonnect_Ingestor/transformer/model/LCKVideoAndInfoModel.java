package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
public class LCKVideoAndInfoModel {
    private String videoTitle;
    private String videoId;
    private LCKTeamModel winTeam;
    private LCKTeamModel loseTeam;
    private LocalDate date;
    private Thumbnail medium;
    private Thumbnail high;
    private Thumbnail standard;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Thumbnail {
        private String url;
        private int width;
        private int height;
    }
}
