package io.github.gyu_young_park.LCKonnect_Ingestor.merger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LCKCrawlAndYoutubeMapModel {
    @JsonProperty("name")
    private String name;

    @JsonProperty("crawl_list")
    private List<String> crawlList;

    @JsonProperty("youtube_list")
    private List<String> youtubeList;

    @JsonProperty("crawl_match_filter_list")
    private List<String> crawlMatchDataFilterList;

    @JsonProperty("crawl_match_pick_id_list")
    private List<String> crawlMatchPickIdList;

    @JsonProperty("crawl_game_filter_list")
    private List<String> crawlGameFilterList;
}
