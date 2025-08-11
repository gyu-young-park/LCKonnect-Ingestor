package io.github.gyu_young_park.LCKonnect_Ingestor.youtube.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListItemListRespDTO;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import org.mapstruct.Mapper;
import org.mapstruct.ap.shaded.freemarker.template.utility.NullArgumentException;

import java.util.NoSuchElementException;

@Mapper(componentModel = "spring", uses = { LCKVideoMapper.class })
public interface LCKPlayListMapper {
    default LCKPlayListModel toModel(LCKPlayListItemListRespDTO lckPlayListItemListRespDTO) {
        if (lckPlayListItemListRespDTO == null) {
            throw new NullArgumentException("LCKPlayListItemListRespDTO is null");
        }

        LCKPlayListModel lckPlayListModel = new LCKPlayListModel();

        if (lckPlayListItemListRespDTO.getItems() != null && !lckPlayListItemListRespDTO.getItems().isEmpty()) {
            var first = lckPlayListItemListRespDTO.getItems().getFirst();
            lckPlayListModel.setPlaylistName(first.getSnippet().getTitle());
            lckPlayListModel.setPlayListId(first.getSnippet().getPlaylistId());
        } else {
            throw new NoSuchElementException("LCKPlayListItemListRespDTO item is empty, please check your request");
        }

        lckPlayListModel.setLckVideoList(
                lckPlayListItemListRespDTO.getItems().stream()
                                .map(this::mapLCKVideoModel) // 아래 메서드 사용
                                .toList()
        );

        return lckPlayListModel;
    }

    // 메서드 시그니처가 이미 구현된 mpastruct라면 적용된다.
    LCKVideoModel mapLCKVideoModel(LCKPlayListItemListRespDTO.PlaylistVideo video);
}
