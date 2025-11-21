package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("transform")
public class TransformConfiguration {
    private String mapDataPath;
}
