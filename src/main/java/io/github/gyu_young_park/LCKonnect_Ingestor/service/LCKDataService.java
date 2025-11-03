package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor.DataModelToEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.ChampionshipEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.TeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.ChampionshipEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.MatchTeamEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.TeamEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.LCKDataMerger;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKTeamModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKVideoAndInfoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataService.class);
    final private LCKDataMerger lckDataMerger;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;
    final private ChampionshipEntityRepository championshipEntityRepository;
    final private TeamEntityRepository teamEntityRepository;
    final private MatchTeamEntityRepository matchTeamEntityRepository;
    final private DataModelToEntity<LCKChampionshipModel, ChampionshipEntity> lckChampionshipModelToChampionshipEntityConvertor;
    final private DataModelToEntity<LCKTeamModel, TeamEntity> lckTeamModelToTeamEntityConvertor;
    final private DataModelToEntity<LCKTeamModel, MatchTeamEntity> lckTeamModelToMatchTeamEntityConvertor;
    final private DataModelToEntity<LCKVideoAndInfoModel, MatchEntity> lckVideoAndInfoModelToMatchEntityConvertor;
    final private Map<String, Boolean> teamEntityCheck = new HashMap<>();

    @Transactional
    public List<LCKChampionshipModel> getLCKData() {
        LCKIngredient ingredient = getIngredient();
        List<LCKChampionshipModel> lckChampionshipModelList = lckDataMerger.merge(ingredient.lckCrawlRawData, ingredient.lckYoutubeModel);
        for (LCKChampionshipModel lckChampionshipModel : lckChampionshipModelList) {
            ChampionshipEntity championshipEntity = lckChampionshipModelToChampionshipEntityConvertor.convert(lckChampionshipModel);

            for (LCKVideoAndInfoModel lckVideoAndInfoModel: lckChampionshipModel.getLckVideoAndInfoModelList()) {
                LOGGER.info("Convert team model to entity: {}, winteam: {}, loseteam: {}", lckVideoAndInfoModel.getVideoTitle(),
                        lckVideoAndInfoModel.getWinTeam().getTeamName(),
                        lckVideoAndInfoModel.getLoseTeam().getTeamName());
                championshipEntity.appendMatch(
                        createMatchEntity(lckVideoAndInfoModel,
                        createAndStoreTeamEntity(lckVideoAndInfoModel.getWinTeam()),
                        createAndStoreTeamEntity(lckVideoAndInfoModel.getLoseTeam())));
            }
            championshipEntityRepository.save(championshipEntity);
        }
        return lckChampionshipModelList;
    }

    private LCKIngredient getIngredient() {
        try(ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            LOGGER.info("Start transform");
            Future<LCKCrawlRawData> lckCrawlRawDataFuture = executorService.submit(new Callable<>() {
                @Override
                public LCKCrawlRawData call() throws Exception {
                    return lckCrawler.crawl();
                }
            });

            LOGGER.info("Start lck youtubue fetcher");
            Future<LCKYoutubeModel> lckYoutubeModelFuture = executorService.submit(new Callable<>() {
                @Override
                public LCKYoutubeModel call() throws Exception {
                    return lckYoutubeFetcher.fetch();
                }
            });

            try {
                LCKCrawlRawData lckCrawlRawData = lckCrawlRawDataFuture.get();
                LCKYoutubeModel lckYoutubeModel = lckYoutubeModelFuture.get();
                return new LCKIngredient(lckCrawlRawData, lckYoutubeModel);
            } catch (InterruptedException | ExecutionException  e) {
                LOGGER.error("Failed to get lck data ingredient task in service");
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    private TeamEntity createAndStoreTeamEntity(LCKTeamModel lckTeamModel) {
        TeamEntity teamEntity = lckTeamModelToTeamEntityConvertor.convert(lckTeamModel);
        storeTeamEntity(teamEntity);
        return teamEntity;
    }

    @Transactional
    private void storeTeamEntity(TeamEntity teamEntity) {
        if (teamEntityCheck.containsKey(teamEntity.getName())) {
            return;
        }
        teamEntityRepository.save(teamEntity);
        teamEntityCheck.put(teamEntity.getName(), Boolean.TRUE);
    }

    private MatchEntity createMatchEntity(LCKVideoAndInfoModel lckVideoAndInfoModel, TeamEntity winTeamEntity, TeamEntity loseTeamEntity) {
        MatchTeamEntity winMatchTeamEntity = lckTeamModelToMatchTeamEntityConvertor.convert(lckVideoAndInfoModel.getWinTeam());
        winMatchTeamEntity.setTeamEntity(winTeamEntity);
        winMatchTeamEntity.setResult(TeamResultEnum.WIN);

        MatchTeamEntity loseMatchTeamEntity = lckTeamModelToMatchTeamEntityConvertor.convert(lckVideoAndInfoModel.getLoseTeam());
        loseMatchTeamEntity.setTeamEntity(loseTeamEntity);
        loseMatchTeamEntity.setResult(TeamResultEnum.LOSE);

        MatchEntity matchEntity = lckVideoAndInfoModelToMatchEntityConvertor.convert(lckVideoAndInfoModel);
        matchEntity.addMatchTeamEntity(winMatchTeamEntity);
        matchEntity.addMatchTeamEntity(loseMatchTeamEntity);
        return matchEntity;
    }

    @AllArgsConstructor
    private static class LCKIngredient {
        LCKCrawlRawData lckCrawlRawData;
        LCKYoutubeModel lckYoutubeModel;
    }

    public List<String> queryWinTeamAndChampion(TeamResultEnum resultEnum, String team, String champion) {
        List<MatchTeamEntity> matchTeamEntityList = matchTeamEntityRepository.findByTeamResultAndChampion(resultEnum,team, champion);
        List<String> videoIds = new ArrayList<>();
        for (var matchTeamEntity: matchTeamEntityList) {
            videoIds.add(matchTeamEntity.getMatchEntity().getVideoId());
        }
        return videoIds;
    }
}
