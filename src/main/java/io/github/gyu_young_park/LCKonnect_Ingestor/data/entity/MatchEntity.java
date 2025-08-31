package io.github.gyu_young_park.LCKonnect_Ingestor.data.entity;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Thumbnail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "matches")
@Entity(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String videoId;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "championship_id")
    private ChampionshipEntity championship;

    @OneToMany(mappedBy = "matchEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchTeamEntity> matchTeamEntityList = new ArrayList();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "high_url")),
            @AttributeOverride(name = "width", column = @Column(name = "high_width")),
            @AttributeOverride(name = "height", column = @Column(name = "high_height"))
    })
    private Thumbnail high;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "medium_url")),
            @AttributeOverride(name = "width", column = @Column(name = "medium_width")),
            @AttributeOverride(name = "height", column = @Column(name = "medium_height"))
    })
    private Thumbnail medium;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "standard_url")),
            @AttributeOverride(name = "width", column = @Column(name = "standard_width")),
            @AttributeOverride(name = "height", column = @Column(name = "standard_height"))
    })
    private Thumbnail standard;

    public void addMatchTeamEntity(MatchTeamEntity matchTeamEntity) {
        matchTeamEntityList.add(matchTeamEntity);
        matchTeamEntity.setMatchEntity(this);
    }

    public void addAllMatchTeamEntityList(List<MatchTeamEntity> matchTeamEntityList) {
        matchTeamEntityList.forEach(this::addMatchTeamEntity);
    }
}
