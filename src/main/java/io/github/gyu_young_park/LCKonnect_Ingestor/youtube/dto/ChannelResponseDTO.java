package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChannelResponseDTO {
    private String kind;
    private String etag;
    private PageInfo pageInfo;
    private List<ChannelItem> items;

    @Data
    public static class PageInfo {
        private int totalResults;
        private int resultsPerPage;
    }

    @Data
    public static class ChannelItem {
        private String kind;
        private String etag;
        private String id;
        private Snippet snippet;
    }

    @Data
    public static class Snippet {
        private String title;
        private String description;
        private String customUrl;
        private String publishedAt;
        private Thumbnails thumbnails;
        private Localized localized;
    }

    @Data
    public static class Thumbnails {
        private Thumbnail defaultThumbnail;
        private Thumbnail medium;
        private Thumbnail high;
    }

    @Data
    public static class Thumbnail {
        private String url;
        private int width;
        private int height;
    }

    @Data
    public static class Localized {
        private String title;
        private String description;
    }
}
