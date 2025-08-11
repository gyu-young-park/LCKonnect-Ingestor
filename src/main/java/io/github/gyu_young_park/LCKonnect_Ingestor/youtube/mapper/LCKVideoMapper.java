package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.ThumbnailModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LCKVideoMapper {

    @Mapping(source = "snippet.title", target = "title")
    @Mapping(source = "snippet.resourceId.videoId", target = "videoId")
    @Mapping(source = "snippet.thumbnails.defaultThumbnail", target = "defaultThumbnail")
    @Mapping(source = "snippet.thumbnails.medium", target = "medium")
    @Mapping(source = "snippet.thumbnails.high", target = "high")
    @Mapping(source = "snippet.thumbnails.standard", target = "standard")
    LCKVideoModel toModel(LCKPlayListItemListRespDTO.PlaylistVideo playlistVideo);

    List<LCKVideoModel> toModelList(List<LCKPlayListItemListRespDTO.PlaylistVideo> playlistVideoList);

    ThumbnailModel toThumbnailModel(LCKPlayListItemListRespDTO.PlaylistVideo.Snippet.Thumbnails.ThumbnailDetail detail);
}
