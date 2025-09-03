package io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.ChampionshipEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import org.springframework.stereotype.Component;

@Component
public class LCKChampionshipModelToChampionshipEntityConvertor implements DataModelToEntity<LCKChampionshipModel, ChampionshipEntity> {
    @Override
    public ChampionshipEntity convert(LCKChampionshipModel lckChampionshipModel) {
        ChampionshipEntity championshipEntity = new ChampionshipEntity();
        championshipEntity.setName(lckChampionshipModel.getChampionshipName());
        championshipEntity.setStartDate(lckChampionshipModel.getStartDate());
        championshipEntity.setLastDate(lckChampionshipModel.getLastDate());
        return championshipEntity;
    }
}
