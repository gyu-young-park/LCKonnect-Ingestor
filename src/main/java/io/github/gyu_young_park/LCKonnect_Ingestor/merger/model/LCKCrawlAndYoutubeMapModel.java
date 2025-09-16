package io.github.gyu_young_park.LCKonnect_Ingestor.merger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LCKCrawlAndYoutubeMapModel {
    @JsonProperty("mappings")
    private Mappings mappings;

    @Getter
    @Setter
    public static class Mappings {
        @JsonProperty("name")
        private String name;

        @JsonProperty("crawl_list")
        private List<String> crawlList;

        @JsonProperty("youtube_list")
        private List<String> youtubeList;
    }
}
