package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { LCKVideoMapper.class })
public interface LCKPlayListMapper {
    default LCKPlayListModel toModel(LCKPlayListItemListRespDTO dto) {
        if (dto == null) return null;

        LCKPlayListModel model = new LCKPlayListModel();

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            var first = dto.getItems().getFirst();
            model.setPlaylistName(first.getSnippet().getTitle());
            model.setPlayListId(first.getSnippet().getPlaylistId());
        }

        model.setLckVideoList(
                dto.getItems() == null ? List.of() :
                        dto.getItems().stream()
                                .map(this::mapLCKVideoModel) // 아래 메서드 사용
                                .toList()
        );

        return model;
    }

    // 메서드 시그니처가 이미 구현된 mpastruct라면 적용된다.
    LCKVideoModel mapLCKVideoModel(LCKPlayListItemListRespDTO.PlaylistVideo video);
}
