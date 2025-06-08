package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LCKMatchRawDataDTO {
    private String id;
    private String leftTeam;
    private int leftTeamTotalScore;
    private String rightTeam;
    private int rightTeamTotalScore;
    private boolean isPlayed;
    private LocalDate date;
    private List<LCKGameRawDataDTO> lckGameRawDataDTOList;
}
