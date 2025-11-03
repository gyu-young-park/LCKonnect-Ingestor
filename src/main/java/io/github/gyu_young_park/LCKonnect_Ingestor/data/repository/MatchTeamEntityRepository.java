package io.github.gyu_young_park.LCKonnect_Ingestor.data.repository;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchTeamEntityRepository extends JpaRepository<MatchTeamEntity, Long> {
    @Query("""
        SELECT mte
        FROM match_team_relationship mte
        JOIN FETCH mte.teamEntity t
        JOIN FETCH mte.matchEntity mt
        JOIN FETCH mt.championship c
        WHERE t.name = :team
          AND mte.result = :result
          AND (
               mte.picks.top = :champion
            OR mte.picks.jungle = :champion
            OR mte.picks.mid = :champion
            OR mte.picks.ad = :champion
            OR mte.picks.supporter = :champion
          )""")
    List<MatchTeamEntity> findByTeamResultAndChampion(
            @Param("result") TeamResultEnum result,
            @Param("team") String team,
            @Param("champion") String champion);
}
