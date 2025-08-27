package io.github.gyu_young_park.LCKonnect_Ingestor.data.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Champions {
    private String top;
    private String jungle;
    private String mid;
    private String ad;
    private String supporter;
}
