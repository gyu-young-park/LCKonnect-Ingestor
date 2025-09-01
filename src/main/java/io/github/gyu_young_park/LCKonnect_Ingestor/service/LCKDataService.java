package io.github.gyu_young_park.LCKonnect_Ingestor.service;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.LCKCrawler;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKCrawlRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.model.LCKLeagueRawData;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor.DataModelToEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.ChampionshipEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchTeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.TeamEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.ChampionshipEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.repository.TeamEntityRepository;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Champions;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.TeamResultEnum;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Thumbnail;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LCKDataService {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKDataService.class);
    final private LCKDataMerger lckDataMerger;
    final private LCKYoutubeFetcher lckYoutubeFetcher;
    final private LCKCrawler lckCrawler;
    final private ChampionshipEntityRepository championshipEntityRepository;
    final private TeamEntityRepository teamEntityRepository;
    final private DataModelToEntity<LCKTeamModel, TeamEntity> lckTeamModelToTeamEntityConvertor;
    private Map<String, Boolean> teamEntityCheck = new HashMap<>();

    public List<LCKChampionshipModel> getLCKData() {
        LOGGER.info("Start transform");
        LCKCrawlRawData lckCrawlRawData = lckCrawler.crawl();

        LOGGER.info("Start lck youtubue fetcher");
        LCKYoutubeModel lckYoutubeModel = lckYoutubeFetcher.fetch();

        List<LCKChampionshipModel> mergedLCKChampionshipModelList = lckDataMerger.merge(lckCrawlRawData, lckYoutubeModel);
        for (LCKChampionshipModel mergedLCKChampionshipModel : mergedLCKChampionshipModelList) {
            ChampionshipEntity championshipEntity = new ChampionshipEntity();
            championshipEntity.setName(mergedLCKChampionshipModel.getChampionshipName());
            championshipEntity.setStartDate(mergedLCKChampionshipModel.getStartDate());
            championshipEntity.setLastDate(mergedLCKChampionshipModel.getLastDate());

            for (LCKVideoAndInfoModel lckVideoAndInfoModel: mergedLCKChampionshipModel.getLckVideoAndInfoModelList()) {
                TeamEntity winTeamEntity = lckTeamModelToTeamEntityConvertor.convert(lckVideoAndInfoModel.getWinTeam());
                TeamEntity loseTeamEntity = lckTeamModelToTeamEntityConvertor.convert(lckVideoAndInfoModel.getLoseTeam());

                storeTeamEntity(winTeamEntity);
                storeTeamEntity(loseTeamEntity);

                MatchTeamEntity winMatchTeamEntity = new MatchTeamEntity();
                winMatchTeamEntity.setTeamEntity(winTeamEntity);
                winMatchTeamEntity.setResult(TeamResultEnum.WIN);
                winMatchTeamEntity.setBans(makeChampions(lckVideoAndInfoModel.getWinTeam().getBanList()));
                winMatchTeamEntity.setPicks(makeChampions(lckVideoAndInfoModel.getWinTeam().getPickList()));

                MatchTeamEntity loseMatchTeamEntity = new MatchTeamEntity();
                loseMatchTeamEntity.setTeamEntity(loseTeamEntity);
                loseMatchTeamEntity.setResult(TeamResultEnum.LOSE);
                loseMatchTeamEntity.setBans(makeChampions(lckVideoAndInfoModel.getLoseTeam().getBanList()));
                loseMatchTeamEntity.setPicks(makeChampions(lckVideoAndInfoModel.getLoseTeam().getPickList()));

                MatchEntity matchEntity = new MatchEntity();
                matchEntity.setTitle(lckVideoAndInfoModel.getVideoTitle());
                matchEntity.setDate(lckVideoAndInfoModel.getDate());
                matchEntity.setVideoId(lckVideoAndInfoModel.getVideoId());
                matchEntity.setHigh(new Thumbnail(lckVideoAndInfoModel.getHigh().getUrl(),
                        lckVideoAndInfoModel.getHigh().getWidth(),
                        lckVideoAndInfoModel.getHigh().getHeight()));
                matchEntity.setMedium(new Thumbnail(lckVideoAndInfoModel.getMedium().getUrl(),
                        lckVideoAndInfoModel.getMedium().getWidth(),
                        lckVideoAndInfoModel.getMedium().getHeight()));
                matchEntity.setStandard(new Thumbnail(lckVideoAndInfoModel.getStandard().getUrl(),
                        lckVideoAndInfoModel.getStandard().getWidth(),
                        lckVideoAndInfoModel.getStandard().getHeight()));
                matchEntity.addMatchTeamEntity(winMatchTeamEntity);
                matchEntity.addMatchTeamEntity(loseMatchTeamEntity);

                championshipEntity.appendMatch(matchEntity);
            }
            championshipEntityRepository.save(championshipEntity);
        }

        return mergedLCKChampionshipModelList;
    }

    private void storeTeamEntity(TeamEntity teamEntity) {
        if (teamEntityCheck.containsKey(teamEntity.getName())) {
            return;
        }
        teamEntityRepository.save(teamEntity);
        teamEntityCheck.put(teamEntity.getName(), Boolean.TRUE);
    }

    private Champions makeChampions(List<String> championList) {
        return new Champions(
                championList.get(0),
                championList.get(1),
                championList.get(2),
                championList.get(3),
                championList.get(4)
        );
    }
}
