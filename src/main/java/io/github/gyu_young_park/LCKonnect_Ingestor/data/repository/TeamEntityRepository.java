package io.github.gyu_young_park.LCKonnect_Ingestor.data.repository;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEntityRepository extends JpaRepository<TeamEntity, Long> {

}
