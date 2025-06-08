package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LCKLeagueRawDataDTO {
    private String league;
    private List<LCKMatchRawDataDTO> lckMatchRawDataDTOList;
}
