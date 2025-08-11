package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LCKPlayListModel {
    private String playlistName;
    private String playListId;
    private List<LCKVideoModel> lckVideoList;
}
