package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.ThumbnailModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LCKVideoMapper {

    @Mapping(source = "title", target = "title")
    @Mapping(source = "resourceId.videoId", target = "videoId")
    @Mapping(source = "thumbnails.defaultThumbnail", target = "defaultThumbnail")
    @Mapping(source = "thumbnails.medium", target = "medium")
    @Mapping(source = "thumbnails.high", target = "high")
    @Mapping(source = "thumbnails.standard", target = "standard")
    LCKVideoModel toModel(LCKPlayListItemListRespDTO.PlaylistVideo.Snippet videoSnippet);

    List<LCKVideoModel> toModelList(List<LCKPlayListItemListRespDTO.PlaylistVideo.Snippet> dtoList);

    ThumbnailModel toThumbnailModel(LCKPlayListItemListRespDTO.PlaylistVideo.Snippet.Thumbnails.ThumbnailDetail detail);
}
