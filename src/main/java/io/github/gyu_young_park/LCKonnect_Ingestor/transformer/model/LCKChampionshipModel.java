package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LCKChampionshipModel {
    private String championshipName;
    private LocalDate startDate;
    private LocalDate lastDate;
    private List<LCKVideoAndInfoModel> lckVideoAndInfoModelList;
}
