package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.Data;

@Data
public class LCKVideoModel {
    private String title;
    private String videoId;
    private ThumbnailModel defaultThumbnail;
    private ThumbnailModel medium;
    private ThumbnailModel high;
    private ThumbnailModel standard;
}
