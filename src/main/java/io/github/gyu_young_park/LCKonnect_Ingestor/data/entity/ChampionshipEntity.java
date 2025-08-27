package io.github.gyu_young_park.LCKonnect_Ingestor.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "championship")
@Table(name = "championship")
public class ChampionshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate lastDate;

    @OneToMany(mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MatchEntity> matches = new ArrayList<>();

    public void appendMatch(MatchEntity matchEntity) {
        matches.add(matchEntity);
        matchEntity.setChampionship(this);
    }

    public void appendMatches(List<MatchEntity> matches) {
        matches.forEach(this::appendMatch);
    }
}
