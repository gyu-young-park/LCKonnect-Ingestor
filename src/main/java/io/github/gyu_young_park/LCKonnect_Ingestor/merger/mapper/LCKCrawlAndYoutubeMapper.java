package io.github.gyu_young_park.LCKonnect_Ingestor.merger.mapper;

import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKCrawlAndYoutubeMapModel;

import java.util.List;

public interface LCKCrawlAndYoutubeMapper {
    List<LCKCrawlAndYoutubeMapModel> get();
}
