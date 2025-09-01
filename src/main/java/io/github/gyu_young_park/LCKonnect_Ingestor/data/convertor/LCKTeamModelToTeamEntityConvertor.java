package io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.TeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKTeamModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LCKTeamModelToTeamEntityConvertor implements DataModelToEntity<LCKTeamModel, TeamEntity>{
    final private Map<String, TeamEntity> teamEntityCache = new HashMap<>();

    @Override
    public TeamEntity convert(LCKTeamModel lckTeamModel) {
        return makeTeamEntity(lckTeamModel.getTeamName());
    }

    private TeamEntity makeTeamEntity(String teamName) {
        if (teamEntityCache.containsKey(teamName)) {
            return teamEntityCache.get(teamName);
        }
        return createTeamEntity(teamName);
    }

    private TeamEntity createTeamEntity(String teamName) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(teamName);
        teamEntityCache.put(teamName,teamEntity);
        return teamEntity;
    }
}
