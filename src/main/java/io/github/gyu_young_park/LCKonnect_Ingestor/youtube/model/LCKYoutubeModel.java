package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LCKYoutubeModel {
    private String name;
    private List<LCKPlayListModel> lckPlayListList;
}
