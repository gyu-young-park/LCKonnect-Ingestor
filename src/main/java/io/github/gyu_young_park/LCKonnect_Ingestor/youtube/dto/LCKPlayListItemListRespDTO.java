package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto;

import lombok.Data;

import java.util.List;

@Data
public class LCKPlayListItemListRespDTO {
    private String kind;
    private String etag;
    private String nextPageToken;
    private List<PlaylistItem> items;
    private PageInfo pageInfo;

    @Data
    public static class PlaylistItem {
        private String kind;
        private String etag;
        private String id;
        private Snippet snippet;

        @Data
        public static class Snippet {
            private String publishedAt;
            private String channelId;
            private String title;
            private String description;
            private Thumbnails thumbnails;
            private String channelTitle;
            private String playlistId;
            private int position;
            private ResourceId resourceId;
            private String videoOwnerChannelTitle;
            private String videoOwnerChannelId;

            @Data
            public static class Thumbnails {
                private ThumbnailDetail defaultThumbnail;
                private ThumbnailDetail medium;
                private ThumbnailDetail high;
                private ThumbnailDetail standard;
                private ThumbnailDetail maxres;

                @Data
                public static class ThumbnailDetail {
                    private String url;
                    private int width;
                    private int height;
                }
            }

            @Data
            public static class ResourceId {
                private String kind;
                private String videoId;
            }
        }
    }

    @Data
    public static class PageInfo {
        private int totalResults;
        private int resultsPerPage;
    }
}
