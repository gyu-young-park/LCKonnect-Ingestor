package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.Data;

import java.util.List;

@Data
public class LCKPlayListModel {
    private String playlistName;
    private String playListId;
    private List<LCKVideoModel> lckVideoList;
}
