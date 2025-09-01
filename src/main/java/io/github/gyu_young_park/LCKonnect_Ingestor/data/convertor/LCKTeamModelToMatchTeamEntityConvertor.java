package io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor;


import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Champions;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKTeamModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LCKTeamModelToMatchTeamEntityConvertor implements DataModelToEntity<LCKTeamModel, MatchTeamEntity> {

    @Override
    public MatchTeamEntity convert(LCKTeamModel lckTeamModel) {
        MatchTeamEntity matchTeamEntity = new MatchTeamEntity();
        matchTeamEntity.setBans(makeChampions(lckTeamModel.getBanList()));
        matchTeamEntity.setPicks(makeChampions(lckTeamModel.getPickList()));
        return matchTeamEntity;
    }

    private Champions makeChampions(List<String> championList) {
        return new Champions(
                championList.get(0),
                championList.get(1),
                championList.get(2),
                championList.get(3),
                championList.get(4)
        );
    }
}
