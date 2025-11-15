package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.dto.response.LCKMatchAndVideoResp;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import io.github.gyu_young_park.LCKonnect_Ingestor.service.LCKDataService;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lckdata")
@RequiredArgsConstructor
public class LCKDataController {
    final private LCKDataService lckDataService;

    @GetMapping()
    public List<LCKChampionshipModel> getLCKData() {
        return lckDataService.getLCKData();
    }

    @GetMapping("/query/win/{team}/{champion}")
    public List<LCKMatchAndVideoResp> getQueryData(
            @PathVariable("team") String team,
            @PathVariable("champion") String champion) {
        System.out.println("team: " + team);
        System.out.println("champion: " + champion);
        return lckDataService.queryWinTeamAndChampion(TeamResultEnum.WIN, team,champion);
    }
}
