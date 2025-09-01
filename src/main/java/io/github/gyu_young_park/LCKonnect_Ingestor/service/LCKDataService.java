package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor.DataModelToEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.ChampionshipEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.TeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.ChampionshipEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.TeamEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.LCKDataMerger;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKTeamModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKVideoAndInfoModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKChampionshipModel;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.fetcher.LCKYoutubeFetcher;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataService.class);
    final private LCKDataMerger lckDataMerger;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;
    final private ChampionshipEntityRepository championshipEntityRepository;
    final private TeamEntityRepository teamEntityRepository;
    final private DataModelToEntity<LCKChampionshipModel, ChampionshipEntity> lckChampionshipModelToChampionshipEntityConvertor;
    final private DataModelToEntity<LCKTeamModel, TeamEntity> lckTeamModelToTeamEntityConvertor;
    final private DataModelToEntity<LCKTeamModel, MatchTeamEntity> lckTeamModelToMatchTeamEntityConvertor;
    final private DataModelToEntity<LCKVideoAndInfoModel, MatchEntity> lckVideoAndInfoModelToMatchEntityConvertor;
    final private Map<String, Boolean> teamEntityCheck = new HashMap<>();

    public List<LCKChampionshipModel> getLCKData() {
        LOGGER.info("Start transform");
        LCKCrawlRawData lckCrawlRawData = lckCrawler.crawl();

        LOGGER.info("Start lck youtubue fetcher");
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();

        List<LCKChampionshipModel> lckChampionshipModelList = lckDataMerger.merge(lckCrawlRawData, lckYoutubeModel);
        for (LCKChampionshipModel lckChampionshipModel : lckChampionshipModelList) {
            ChampionshipEntity championshipEntity = lckChampionshipModelToChampionshipEntityConvertor.convert(lckChampionshipModel);

            for (LCKVideoAndInfoModel lckVideoAndInfoModel: lckChampionshipModel.getLckVideoAndInfoModelList()) {
                TeamEntity winTeamEntity = lckTeamModelToTeamEntityConvertor.convert(lckVideoAndInfoModel.getWinTeam());
                TeamEntity loseTeamEntity = lckTeamModelToTeamEntityConvertor.convert(lckVideoAndInfoModel.getLoseTeam());
                storeTeamEntity(winTeamEntity);
                storeTeamEntity(loseTeamEntity);

                championshipEntity.appendMatch(createMatchEntity(lckVideoAndInfoModel, winTeamEntity, loseTeamEntity));
            }
            championshipEntityRepository.save(championshipEntity);
        }

        return lckChampionshipModelList;
    }

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

}
