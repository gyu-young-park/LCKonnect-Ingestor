package io.github.gyu_young_park.LCKonnect_Ingestor.transformer.merger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.TransformConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKGameRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKTeamModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.transformer.model.LCKVideoAndInfoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Data
@RequiredArgsConstructor
public class LCKDataMerger {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataMerger.class);
    final private TransformConfiguration transformConfiguration;

    public List<LCKChampionshipModel> merge(List<LCKLeagueRawData> lckLeagueRawDataList, LCKYoutubeModel lckYoutubeModel) {
        Map<String, List<LCKMatchRawData>> lckCrawlDataWithLeagueNameMap = new HashMap<>();
        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            lckCrawlDataWithLeagueNameMap.put(lckLeagueRawData.getLeague().trim(), lckLeagueRawData.getLckMatchRawDataList());
        }

        Map<String, List<LCKVideoModel>> lckVideoDataWithLeagueNameMap = new HashMap<>();
        for (LCKPlayListModel lckPlayListModel: lckYoutubeModel.getLckPlayListList()) {
            lckVideoDataWithLeagueNameMap.put(lckPlayListModel.getPlaylistName().trim(), lckPlayListModel.getLckVideoList());
        }

        // TODO: refactor
        List<LCKChampionshipModel> lckChampionshipModelList = new ArrayList<>();
        Map<String, String> lckCrawlDataAndLCKVideoDataMap = getLCKCrawlDataAndLCKVideoData();
        for (Map.Entry<String, String> lckCrawlDataAndLCKVideoDataEntry : lckCrawlDataAndLCKVideoDataMap.entrySet()) {
            String lckCrawlDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getKey();
            String lckVideoDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getValue();
            List<LCKMatchRawData> lckMatchRawDataList = lckCrawlDataWithLeagueNameMap.get(lckCrawlDataLeagueName);
            if (lckMatchRawDataList == null || lckMatchRawDataList.isEmpty()) {
                throw new NoSuchElementException("LCK Crawl Data league is not matched with: " + lckCrawlDataLeagueName);
            }

            int videoIndex = 0;
            List<LCKVideoModel> lckVideoModelList = lckVideoDataWithLeagueNameMap.get(lckVideoDataLeagueName);
            if (lckVideoModelList == null || lckVideoModelList.isEmpty()) {
                throw new NoSuchElementException("LCK Youtube Data league is not matched with: " + lckVideoDataLeagueName);
            }

            // TODO1: lckChampionshipModel 시작
            LCKChampionshipModel lckChampionshipModel = new LCKChampionshipModel();
            lckChampionshipModel.setChampionshipName(lckVideoDataLeagueName);

            // TODO2: List<LCKVideoAndInfoModel> 시작
            List<LCKVideoAndInfoModel> lckVideoAndInfoModelList = new ArrayList<>();
            for (LCKMatchRawData lckMatchRawData : lckMatchRawDataList) {
                if (videoIndex == 0) {
                    lckChampionshipModel.setStartDate(lckMatchRawData.getDate());
                }
                lckChampionshipModel.setLastDate(lckMatchRawData.getDate());

                List<LCKGameRawData> lckGameRawDataList = lckMatchRawData.getLckGameRawDataList();
                for (int i = lckGameRawDataList.size() - 1; i >= 0; i--) {
                    // // TODO3: LCKVideoAndInfoModel 시작
                    LCKVideoAndInfoModel lckVideoAndInfoModel = new LCKVideoAndInfoModel();
                    LCKGameRawData lckGameRawData = lckGameRawDataList.get(i);
                    LCKVideoModel lckVideoModel = lckVideoModelList.get(videoIndex);

                    if (lckGameRawData.getLeftTeamScore() == 0) {
                        lckVideoAndInfoModel.setLoseTeam(LCKTeamModel.builder()
                                .teamName(lckGameRawData.getLeftTeam())
                                .banList(lckGameRawData.getLeftTeamBans())
                                .pickList(lckGameRawData.getLeftTeamPicks())
                                .build());
                        lckVideoAndInfoModel.setWinTeam(LCKTeamModel.builder()
                                .teamName(lckGameRawData.getRightTeam())
                                .banList(lckGameRawData.getRightTeamBans())
                                .pickList(lckGameRawData.getRightTeamPicks())
                                .build());
                    } else {
                        lckVideoAndInfoModel.setWinTeam(LCKTeamModel.builder()
                                .teamName(lckGameRawData.getLeftTeam())
                                .banList(lckGameRawData.getLeftTeamBans())
                                .pickList(lckGameRawData.getLeftTeamPicks())
                                .build());
                        lckVideoAndInfoModel.setLoseTeam(LCKTeamModel.builder()
                                .teamName(lckGameRawData.getRightTeam())
                                .banList(lckGameRawData.getRightTeamBans())
                                .pickList(lckGameRawData.getRightTeamPicks())
                                .build());
                    }

                    lckVideoAndInfoModel.setDate(lckMatchRawData.getDate());
                    lckVideoAndInfoModel.setVideoId(lckVideoModel.getVideoId());
                    lckVideoAndInfoModel.setVideoTitle(lckVideoModel.getTitle());
                    // 데이터를 json으로 바꾸기
                    lckVideoAndInfoModelList.add(lckVideoAndInfoModel);
                    LOGGER.debug("LCK Game name: " + lckVideoModel.getTitle() + " left team: " + lckGameRawData.getLeftTeam() + " left score: " + lckGameRawData.getLeftTeamScore() + " right team: " + lckGameRawData.getRightTeam() + " right score: " + lckGameRawData.getRightTeamScore());
                    videoIndex++;
                }
                lckChampionshipModel.setLckVideoAndInfoModelList(lckVideoAndInfoModelList);
            }
            lckChampionshipModelList.add(lckChampionshipModel);
        }
        return lckChampionshipModelList;
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
