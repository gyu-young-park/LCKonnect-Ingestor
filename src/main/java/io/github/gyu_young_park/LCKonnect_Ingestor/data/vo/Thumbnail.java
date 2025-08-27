package io.github.gyu_young_park.LCKonnect_Ingestor.data.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Thumbnail {
    private String url;
    private int width;
    private int height;
}
