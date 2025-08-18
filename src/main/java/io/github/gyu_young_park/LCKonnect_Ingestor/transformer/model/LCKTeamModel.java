package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LCKTeamModel {
    private String teamName;
    private List<String> banList;
    private List<String> pickList;
}
