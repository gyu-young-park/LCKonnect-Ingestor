package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.merger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.TransformConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKGameRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Data
@RequiredArgsConstructor
public class LCKDataMerger {
    final private TransformConfiguration transformConfiguration;

    public void merge(List<LCKLeagueRawData> lckLeagueRawDataList, LCKYoutubeModel lckYoutubeModel) {
        Map<String, List<LCKMatchRawData>> lckCrawlDataWithLeagueNameMap = new HashMap<>();
        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            lckCrawlDataWithLeagueNameMap.put(lckLeagueRawData.getLeague().trim(), lckLeagueRawData.getLckMatchRawDataList());
        }

        Map<String, List<LCKVideoModel>> lckVideoDataWithLeagueNameMap = new HashMap<>();
        for (LCKPlayListModel lckPlayListModel: lckYoutubeModel.getLckPlayListList()) {
            lckVideoDataWithLeagueNameMap.put(lckPlayListModel.getPlaylistName().trim(), lckPlayListModel.getLckVideoList());
        }

        Map<String, String> lckCrawlDataAndLCKVideoDataMap = getLCKCrawlDataAndLCKVideoData();
        for (Map.Entry<String, String> lckCrawlDataAndLCKVideoDataEntry : lckCrawlDataAndLCKVideoDataMap.entrySet()) {
            String lckCrawlDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getKey();
            String lckVideoDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getValue();
            List<LCKMatchRawData> lckMatchRawDataList = lckCrawlDataWithLeagueNameMap.get(lckCrawlDataLeagueName);

            int videoIndex = 0;
            List<LCKVideoModel> lckVideoModelList = lckVideoDataWithLeagueNameMap.get(lckVideoDataLeagueName);
            if (lckMatchRawDataList == null) {
                throw new NoSuchElementException("LCK Crawl Data league is not matched with: " + lckCrawlDataLeagueName);
            }

            for (LCKMatchRawData lckMatchRawData : lckMatchRawDataList) {
                List<LCKGameRawData> lckGameRawDataList = lckMatchRawData.getLckGameRawDataList();

                for (int i = lckGameRawDataList.size() - 1; i >= 0; i--) {
                    LCKGameRawData lckGameRawData = lckGameRawDataList.get(i);
                    LCKVideoModel lckVideoModel = lckVideoModelList.get(videoIndex);
                    // 데이터를 json으로 바꾸기
                    System.out.println("LCK Game name: " + lckVideoModel.getTitle() + " left team: " + lckGameRawData.getLeftTeam() + " left score: " + lckGameRawData.getLeftTeamScore() + " right team: " + lckGameRawData.getRightTeam() + " right score: " + lckGameRawData.getRightTeamScore());
                    videoIndex++;
                }
            }
        }
    }

    private Map<String, String> getLCKCrawlDataAndLCKVideoData() {
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
