package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LCKPlayListMapper {
    @Mapping(source = "snippet.title", target = "playlistName")
    @Mapping(source = "id", target = "playListId")
    LCKPlayListModel toModel(LCKPlayListAPIRespDTO.Item playList);
}
