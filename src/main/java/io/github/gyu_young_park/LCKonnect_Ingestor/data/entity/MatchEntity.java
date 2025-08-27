package io.github.gyu_young_park.LCKonnect_Ingestor.data.entity;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Thumbnail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
}
