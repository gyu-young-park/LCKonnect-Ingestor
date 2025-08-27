package io.github.gyu_young_park.LCKonnect_Ingestor.data.entity;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Champions;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "match_team_relationship")
@Table(name = "match_team_relationship")
public class MatchTeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TeamResultEnum result;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchEntity matchEntity;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "top", column = @Column(name = "pick_top")),
            @AttributeOverride(name = "jungle", column = @Column(name = "pick_jungle")),
            @AttributeOverride(name = "mid", column = @Column(name = "pick_mid")),
            @AttributeOverride(name = "ad", column = @Column(name = "pick_ad")),
            @AttributeOverride(name = "supporter", column = @Column(name = "pick_supporter"))
    })
    private Champions picks;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "top", column = @Column(name = "ban_top")),
            @AttributeOverride(name = "jungle", column = @Column(name = "ban_jungle")),
            @AttributeOverride(name = "mid", column = @Column(name = "ban_mid")),
            @AttributeOverride(name = "ad", column = @Column(name = "ban_ad")),
            @AttributeOverride(name = "supporter", column = @Column(name = "ban_supporter"))
    })
    private Champions bans;
}
