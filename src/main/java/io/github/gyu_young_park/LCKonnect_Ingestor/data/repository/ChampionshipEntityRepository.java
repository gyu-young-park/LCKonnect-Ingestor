package io.github.gyu_young_park.LCKonnect_Ingestor.data.repository;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.ChampionshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionshipEntityRepository extends JpaRepository<ChampionshipEntity, Long> {

}
