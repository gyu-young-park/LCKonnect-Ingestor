package io.github.gyu_young_park.LCKonnect_Ingestor.transformer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.TransformConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LCKDataTransformer {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataTransformer.class);
    final private TransformConfiguration transformConfiguration;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;

    public LCKChampionshipModel transform() {
        Map<String, String> mappingData = getMappingData();
        for (String key : mappingData.keySet()) {
            LOGGER.info("key:{}, value:{}", key, mappingData.get(key));
        }

        LOGGER.info("Start transform");
        List<LCKLeagueRawData> lckLeagueRawDataList = lckCrawler.crawl();

        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            LOGGER.info("crawl: {}", lckLeagueRawData.getLeague());
        }

        LOGGER.info("Start lck youtubue fetcher");
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();
        for (LCKPlayListModel lckPlayListModel : lckYoutubeModel.getLckPlayListList()) {
            LOGGER.info("lck yotubue: {}", lckPlayListModel.getPlaylistName());
        }


        return new LCKChampionshipModel();
    }

    private Map<String, String> getMappingData() {
        Map<String, String> crawlAndYoutubeMap = Map.of();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            crawlAndYoutubeMap = objectMapper.readValue(new File(transformConfiguration.getMapDataPath()), new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crawlAndYoutubeMap;
    }
}
