package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class EnvConfigManager {
    private String youtubeAPIkey;

    private EnvConfigManager() {
        Dotenv dotenv = Dotenv.load();
        this.youtubeAPIkey = dotenv.get("YOUTUBE_API_KEY");
    }
}
