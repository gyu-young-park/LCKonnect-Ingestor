package io.github.gyu_young_park.LCKonnect_Ingestor.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;

@Data
public class EnvConfigManager {
    private static EnvConfigManager instance;
    private String youtubeAPIkey;

    private EnvConfigManager() {
        Dotenv dotenv = Dotenv.load();
        this.youtubeAPIkey = dotenv.get("YOUTUBE_API_KEY");
    }

    public static EnvConfigManager getInstance() {
        if (instance == null) {
            instance = new EnvConfigManager();
        }
        return instance;
    }
}
