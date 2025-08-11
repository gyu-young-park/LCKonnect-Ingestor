package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LCKYoutubeModel {
    private String name;
    private int totalPlayList;
    private List<LCKPlayListModel> lckPlayListList;

    public LCKYoutubeModel(LCKYoutubeModel lckYoutubeModel) {
        this.name = lckYoutubeModel.getName();
        this.totalPlayList = lckYoutubeModel.getTotalPlayList();
        this.lckPlayListList = lckYoutubeModel.getLckPlayListList();
    }
}
