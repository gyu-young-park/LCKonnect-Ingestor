package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto;

import lombok.Data;

import java.util.List;

@Data
public class LCKPlayListAPIRespDTO {
    private String kind;
    private String etag;
    private String nextPageToken;
    private String prevPageToken;
    private PageInfo pageInfo;
    private List<Item> items;

    // getters, setters
    @Data
    public static class PageInfo {
        private int totalResults;
        private int resultsPerPage;
        // getters, setters
    }

    @Data
    public static class Item {
        private String kind;
        private String etag;
        private String id;
        private Snippet snippet;
        // getters, setters
    }

    @Data
    public static class Snippet {
        private String publishedAt;
        private String channelId;
        private String title;
        private String description;
        private String channelTitle;
        private Localized localized;
        // getters, setters
    }

    @Data
    public static class Localized {
        private String title;
        private String description;
        // getters, setters
    }
}
