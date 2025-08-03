package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.Data;

import java.util.List;

@Data
public class LCKYoutubeModel {
    private String name;
    private String channelId;
    private int totalPlayList;
    private List<LCKPlayListModel> lckPlayListList;
}
