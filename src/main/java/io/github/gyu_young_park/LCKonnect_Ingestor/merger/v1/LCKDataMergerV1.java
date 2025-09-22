package io.github.gyu_young_park.LCKonnect_Ingestor.merger.v1;

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

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@RequiredArgsConstructor
public class LCKDataMergerV1 implements LCKDataMerger {
    private static final Logger LOGGER = LoggerFactory.getLogger(LCKDataMergerV1.class);

    private final LCKCrawlAndYoutubeMapper lckCrawlAndYoutubeMapper;
    private final TransformConfiguration transformConfiguration;

    private Map<String, List<LCKMatchRawData>> arrangeLCKLeagueRawDataMapWithLeagueKey(List<LCKLeagueRawData> lckLeagueRawDataList) {
        Map<String, List<LCKMatchRawData>> map = new HashMap<>();
        for (LCKLeagueRawData leagueRawData : lckLeagueRawDataList) {
            map.put(leagueRawData.getLeague().trim(), leagueRawData.getLckMatchRawDataList());
        }
        LOGGER.info("Arranged {} leagues from crawl data.", map.size());
        return map;
    }

    private Map<String, List<LCKVideoModel>> arrangeLCKPlayListMapWithPlayListKey(List<LCKPlayListModel> playListModels) {
        Map<String, List<LCKVideoModel>> map = new HashMap<>();
        for (LCKPlayListModel playListModel: playListModels) {
            map.put(playListModel.getPlaylistName().trim(), playListModel.getLckVideoList());
        }
        LOGGER.info("Arranged {} playlists from YouTube data.", map.size());
        return map;
    }

    private LCKVideoAndInfoModel mergeLCKVideoAndInfoModel(LCKVideoModel lckVideoModel, LCKGameRawData lckGameRawData) {
        LOGGER.debug("Merging video [{}] with game [{} vs {}]",
                lckVideoModel.getTitle(), lckGameRawData.getLeftTeam(), lckGameRawData.getRightTeam());

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

        LCKVideoAndInfoModel result = new LCKVideoAndInfoModel();
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
        LOGGER.info("Starting merge of crawl and YouTube data.");

        List<LCKChampionshipModel> championshipList = new ArrayList<>();
        Map<String, List<LCKMatchRawData>> crawlMap = arrangeLCKLeagueRawDataMapWithLeagueKey(lckCrawlRawData.getLckLeagueRawDataList());
        Map<String, List<LCKVideoModel>> videoMap = arrangeLCKPlayListMapWithPlayListKey(lckYoutubeModel.getLckPlayListList());

        for (LCKCrawlAndYoutubeMapModel mapping : lckCrawlAndYoutubeMapper.get()) {
            LOGGER.info("Processing championship [{}]", mapping.getName());

            // 1. Crawl Data 매핑
            List<LCKMatchRawData> matchList = new ArrayList<>();
            for(String leagueName : mapping.getCrawlList()) {
                leagueName = leagueName.trim();
                if (!crawlMap.containsKey(leagueName)) {
                    throw new NoSuchElementException("Missing crawl league: " + leagueName);
                }
                matchList.addAll(crawlMap.get(leagueName));
            }

            // filter: 플레이 되지 않은 match는 걸러낸다. isPlayed가 false인 것만 골라낸다.
            matchList.removeIf(match -> !match.isPlayed());
            LOGGER.info("Collected {} matches for championship [{}]", matchList.size(), mapping.getName());

            // 2. Video Data 매핑
            List<LCKVideoModel> videoList = new ArrayList<>();
            for(String playListName : mapping.getYoutubeList()) {
                if (!videoMap.containsKey(playListName)) {
                    throw new NoSuchElementException("Missing YouTube playlist: " + playListName);
                }
                videoList.addAll(videoMap.get(playListName));
            }
            // filter: Private video 영상 삭제
            videoList.removeIf(video -> video.getTitle().equalsIgnoreCase("Private video"));
            LOGGER.info("Collected {} videos for championship [{}]", videoList.size(), mapping.getName());

            // 3. Championship Model 생성
            LCKChampionshipModel championship = new LCKChampionshipModel();
            championship.setChampionshipName(mapping.getName());

            List<LCKVideoAndInfoModel> videoAndInfoList = new ArrayList<>();
            int videoIndex = 0;

            for (LCKMatchRawData match : matchList) {
                if (videoIndex == 0) {
                    championship.setStartDate(match.getDate());
                }
                championship.setLastDate(match.getDate());

                List<LCKGameRawData> gameList = match.getLckGameRawDataList();
                for (int i = gameList.size() - 1; i >= 0; i--, videoIndex++) {
                    LCKGameRawData game = gameList.get(i);
                    LCKVideoModel video = videoList.get(videoIndex);

                    LOGGER.info("Mapped video [{}] to game [{} vs {}] on {}",
                            video.getTitle(), game.getLeftTeam(), game.getRightTeam(), match.getDate());

                    LCKVideoAndInfoModel videoAndInfo = mergeLCKVideoAndInfoModel(video, game);
                    videoAndInfo.setDate(match.getDate());
                    videoAndInfoList.add(videoAndInfo);
                }
                championship.setLckVideoAndInfoModelList(videoAndInfoList);
            }

            championshipList.add(championship);
            LOGGER.info("Finished processing championship [{}] with {} videos mapped.",
                    mapping.getName(), videoAndInfoList.size());
        }

        LOGGER.info("Completed merge. Total championships: {}", championshipList.size());
        return championshipList;
    }
}
