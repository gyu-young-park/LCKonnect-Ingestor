package io.github.gyu_young_park.LCKonnect_Ingestor.merger.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.TransformConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKGameRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKMatchRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.LCKDataMerger;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.mapper.LCKCrawlAndYoutubeMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKCrawlAndYoutubeMapModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKTeamModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKVideoAndInfoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKPlayListModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKVideoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.ThumbnailModel;
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
public class LCKDataMergerV1 implements LCKDataMerger {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataMergerV1.class);
    final private LCKCrawlAndYoutubeMapper lckCrawlAndYoutubeMapper;
    final private TransformConfiguration transformConfiguration;

    private Map<String, List<LCKMatchRawData>> arrangeLCKLeagueRawDataMapWithLeagueKey(List<LCKLeagueRawData> lckLeagueRawDataList) {
        Map<String, List<LCKMatchRawData>> lckCrawlDataWithLeagueNameMap = new HashMap<>();
        for (LCKLeagueRawData lckLeagueRawData : lckLeagueRawDataList) {
            lckCrawlDataWithLeagueNameMap.put(lckLeagueRawData.getLeague().trim(), lckLeagueRawData.getLckMatchRawDataList());
        }
        return lckCrawlDataWithLeagueNameMap;
    }

    private Map<String, List<LCKVideoModel>> arrangeLCKPlayListMapWithPlayListKey(List<LCKPlayListModel> lckYoutubeModelList) {
        Map<String, List<LCKVideoModel>> lckVideoDataWithLeagueNameMap = new HashMap<>();
        for (LCKPlayListModel lckPlayListModel: lckYoutubeModelList) {
            lckVideoDataWithLeagueNameMap.put(lckPlayListModel.getPlaylistName().trim(), lckPlayListModel.getLckVideoList());
        }
        return lckVideoDataWithLeagueNameMap;
    }

    private LCKVideoAndInfoModel mergeLCKVideoAndInfoModel(LCKVideoModel lckVideoModel, LCKGameRawData lckGameRawData) {
        LCKVideoAndInfoModel result = new LCKVideoAndInfoModel();

        boolean isLeftWin = lckGameRawData.getLeftTeamScore() != 0;
        LCKTeamModel leftTeam = LCKTeamModel.builder()
                .teamName(lckGameRawData.getLeftTeam())
                .banList(lckGameRawData.getLeftTeamBans())
                .pickList(lckGameRawData.getLeftTeamPicks())
                .build();

        LCKTeamModel rightTeam = LCKTeamModel.builder()
                .teamName(lckGameRawData.getRightTeam())
                .banList(lckGameRawData.getRightTeamBans())
                .pickList(lckGameRawData.getRightTeamPicks())
                .build();

        result.setWinTeam(isLeftWin ? leftTeam : rightTeam);
        result.setLoseTeam(isLeftWin ? rightTeam : leftTeam);

        result.setVideoId(lckVideoModel.getVideoId());
        result.setVideoTitle(lckVideoModel.getTitle());

        result.setMedium(toThumbnail(lckVideoModel.getMedium()));
        result.setHigh(toThumbnail(lckVideoModel.getHigh()));
        result.setStandard(toThumbnail(lckVideoModel.getStandard()));

        return result;
    }

    private LCKVideoAndInfoModel.Thumbnail toThumbnail(ThumbnailModel thumbnailModel) {
        return new LCKVideoAndInfoModel.Thumbnail(
                thumbnailModel.getUrl(),
                thumbnailModel.getWidth(),
                thumbnailModel.getHeight()
        );
    }

    public List<LCKChampionshipModel> merge(LCKCrawlRawData lckCrawlRawData, LCKYoutubeModel lckYoutubeModel) {
        List<LCKChampionshipModel> lckChampionshipModelList = new ArrayList<>();
        Map<String, List<LCKMatchRawData>> lckCrawlDataWithLeagueNameMap = arrangeLCKLeagueRawDataMapWithLeagueKey(lckCrawlRawData.getLckLeagueRawDataList());
        Map<String, List<LCKVideoModel>> lckVideoDataWithLeagueNameMap = arrangeLCKPlayListMapWithPlayListKey(lckYoutubeModel.getLckPlayListList());

        for (Map.Entry<String, String> lckCrawlDataAndLCKVideoDataEntry : getLCKCrawlDataAndLCKVideoData().entrySet()) {
            String lckCrawlDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getKey();
            String lckVideoDataLeagueName = lckCrawlDataAndLCKVideoDataEntry.getValue();
            List<LCKMatchRawData> lckMatchRawDataList = lckCrawlDataWithLeagueNameMap.get(lckCrawlDataLeagueName);
            if (lckMatchRawDataList == null || lckMatchRawDataList.isEmpty()) {
                throw new NoSuchElementException("LCK Crawl Data league is not matched with: " + lckCrawlDataLeagueName);
            }

            List<LCKVideoModel> lckVideoModelList = lckVideoDataWithLeagueNameMap.get(lckVideoDataLeagueName);
            if (lckVideoModelList == null || lckVideoModelList.isEmpty()) {
                throw new NoSuchElementException("LCK Youtube Data league is not matched with: " + lckVideoDataLeagueName);
            }

            // TODO1: lckChampionshipModel 시작
            LCKChampionshipModel lckChampionshipModel = new LCKChampionshipModel();
            lckChampionshipModel.setChampionshipName(lckVideoDataLeagueName);

            // TODO2: List<LCKVideoAndInfoModel> 시작
            List<LCKVideoAndInfoModel> lckVideoAndInfoModelList = new ArrayList<>();
            int videoIndex = 0;

            for (LCKMatchRawData lckMatchRawData : lckMatchRawDataList) {
                if (videoIndex == 0) {
                    lckChampionshipModel.setStartDate(lckMatchRawData.getDate());
                }
                lckChampionshipModel.setLastDate(lckMatchRawData.getDate());

                List<LCKGameRawData> lckGameRawDataList = lckMatchRawData.getLckGameRawDataList();
                for (int i = lckGameRawDataList.size() - 1; i >= 0; i--, videoIndex++) {
                    // // TODO3: LCKVideoAndInfoModel 시작
                    LCKGameRawData lckGameRawData = lckGameRawDataList.get(i);
                    LCKVideoModel lckVideoModel = lckVideoModelList.get(videoIndex);
                    LCKVideoAndInfoModel lckVideoAndInfoModel = mergeLCKVideoAndInfoModel(lckVideoModel, lckGameRawData);
                    lckVideoAndInfoModel.setDate(lckMatchRawData.getDate());
                    lckVideoAndInfoModelList.add(lckVideoAndInfoModel);
                    LOGGER.debug("LCK Game name: " + lckVideoModel.getTitle() + " left team: " + lckGameRawData.getLeftTeam() + " left score: " + lckGameRawData.getLeftTeamScore() + " right team: " + lckGameRawData.getRightTeam() + " right score: " + lckGameRawData.getRightTeamScore());
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

    private List<LCKCrawlAndYoutubeMapModel> getLCKCrawlDataAndLCKVideoData2() {
        return lckCrawlAndYoutubeMapper.get();
    }
}
