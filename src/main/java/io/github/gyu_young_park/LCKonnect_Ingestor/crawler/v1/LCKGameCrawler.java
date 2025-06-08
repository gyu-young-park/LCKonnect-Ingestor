package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.dto.LCKGameRawDataDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LCKGameCrawler {
    private static final Logger logger = LoggerFactory.getLogger(LCKGameCrawler.class);
    private final String GAME_ROUND_URL = "https://gol.gg/game/stats/{id}/page-summary/";

    public List<LCKGameRawDataDTO> crawlLCKGameRawDataModelList(String matchId) throws IOException {
        List<LCKGameRawDataDTO> lckGameRawDataDTOList = new ArrayList<>();

        Document doc = Jsoup.connect(buildGameUrlWithMatchId(matchId)).get();
        Pair<String, String> teamPair = parseTeamName(doc);
        List<Element> gameRoundHTMLList = parseGameRoundList(doc);
        List<String> gameIdList = parseGameIdList(doc);

        // verify: gameIdList length must be the same as gameRoundHTMLList
        int round = 1;
        for (Element gameRoundHTML: gameRoundHTMLList) {
            List<Element> imgTagList = gameRoundHTML.select("img.champion_icon_medium").asList();
            LCKGameRawDataDTO lckGameRawDataDTO = new LCKGameRawDataDTO();
            lckGameRawDataDTO.setId(gameIdList.get(round-1));
            lckGameRawDataDTO.setGameRound(round);
            lckGameRawDataDTO.setLeftTeam(teamPair.getFirst());
            lckGameRawDataDTO.setRightTeam(teamPair.getSecond());
            lckGameRawDataDTO.setLeftTeamScore(parseTeamScore(gameRoundHTML, 0));
            lckGameRawDataDTO.setRightTeamScore(parseTeamScore(gameRoundHTML, 1));
            lckGameRawDataDTO.setGameDuration(parseGameTime(gameRoundHTML));
            lckGameRawDataDTO.setLeftTeamBans(parseChampions(imgTagList, 0, 5));
            lckGameRawDataDTO.setLeftTeamPicks(parseChampions(imgTagList, 5, 10));
            lckGameRawDataDTO.setRightTeamBans(parseChampions(imgTagList, 10, 15));
            lckGameRawDataDTO.setRightTeamPicks(parseChampions(imgTagList, 15, 20));
            lckGameRawDataDTOList.add(lckGameRawDataDTO);
            round += 1;
        }

        return lckGameRawDataDTOList;
    }

    private String buildGameUrlWithMatchId(String matchId) {
        return UriComponentsBuilder.fromUriString(GAME_ROUND_URL)
                .buildAndExpand(matchId).toUriString();
    }

    private Pair<String, String> parseTeamName(Document doc) {
        return Pair.of(doc.select("div.col-4 h1 a").get(0).text(), doc.select("div.col-4 h1 a").get(1).text());
    }

    private List<Element> parseGameRoundList(Document doc) {
        return doc.select("div.row.pb-1").asList();
    }

    private int parseTeamScore(Element gameRound, int h1Index) {
        String scoreText = gameRound.select("h1").get(h1Index).text();
        return "WIN".equals(scoreText) ? 1 : 0;
    }

    private Duration parseGameTime(Element rawGameResultHTML) {
        String durationStr = rawGameResultHTML.select("h1").get(1).text(); // 33:07
        String[] minutesAndSeconds = durationStr.split(":");
        if (minutesAndSeconds.length == 3) {
            int hours = Integer.parseInt(minutesAndSeconds[0]);
            int minutes = Integer.parseInt(minutesAndSeconds[1]);
            int seconds = Integer.parseInt(minutesAndSeconds[2]);
            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        }
        int minutes = Integer.parseInt(minutesAndSeconds[0]);
        int seconds = Integer.parseInt(minutesAndSeconds[1]);
        return Duration.ofMillis(minutes).plusSeconds(seconds);
    }

    private List<String> parseChampions(List<Element> imageTags, int start, int end) {
        List<String> champions = new ArrayList<>();
        for (int i = start; i < end && i < imageTags.size(); i++) {
            String champion = extractChampionNameFromSrc(imageTags.get(i).attr("src"));
            champions.add(champion);
        }
        return champions;
    }

    private String extractChampionNameFromSrc(String src) {
        String[] parts = src.split("/");
        String filename = parts[parts.length - 1];
        return filename.replace(".png", "");
    }

    private List<String> parseGameIdList(Document doc) {
        List<String> gameIdList = new ArrayList<>();
        List<Element> gameNavList = doc.select("li.nav-item.game-menu-button").asList();
        for (int i = 1; i < gameNavList.size(); i++) {
            gameIdList.add(gameNavList.get(i).selectFirst("a").attr("href").split("/")[3]);
        }
        return gameIdList;
    }
}
