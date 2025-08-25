package io.github.gyu_young_park.LCKonnect_Ingestor.merger;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;

import java.util.List;

public interface LCKDataMerger {
    List<LCKChampionshipModel> merge(LCKCrawlRawData lckCrawlRawData, LCKYoutubeModel lckYoutubeModel);
}
