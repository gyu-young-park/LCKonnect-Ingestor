package io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1;

import io.github.gyu_young_park.LCKonnect_Ingestor.model.LCKGameRawDataModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LCKGameCrawler {
    private static String GAME_ROUND_URL = "https://gol.gg/game/stats/{id}/page-summary/";

    public List<LCKGameRawDataModel> crawlLCKGameRawDataModelList(String matchId) throws IOException {
        Document doc = Jsoup.connect(buildGameUrlWithMatchId(matchId)).get();
        Pair<String, String> teamPair = parseTeamName(doc);
        List<LCKGameRawDataModel> lckGameRawDataModelList = new ArrayList<>();
        List<Element> gameRoundHTMLList = parseGameRoundList(doc);

        int round = 1;
        for (Element gameRoundHTML: gameRoundHTMLList) {
            LCKGameRawDataModel lckGameRawDataModel = new LCKGameRawDataModel();
            lckGameRawDataModel.setGameRound(round);
            lckGameRawDataModel.setLeftTeam(teamPair.getFirst());
            lckGameRawDataModel.setRightTeam(teamPair.getSecond());
            lckGameRawDataModel.setLeftTeamScore(parseLeftTeamScore(gameRoundHTML));
            lckGameRawDataModel.setRightTeamScore(parseRightTeamScore(gameRoundHTML));
            lckGameRawDataModel.setGameDuration(parseGameTime(gameRoundHTML));
            Elements imageTagList = gameRoundHTML.select("img.champion_icon_medium");

            List<String> leftTeamBans = new ArrayList<>();
            List<String> leftTeamPicks = new ArrayList<>();
            List<String> rightTeamBans = new ArrayList<>();
            List<String> rightTeamPicks = new ArrayList<>();
            for (int i = 0; i < imageTagList.size(); i++) {
                String[] srcPath = imageTagList.get(i).attr("src").split("/");
                String champion = srcPath[srcPath.length - 1].replace(".png", "");
                if (i < 5) {
                    leftTeamBans.add(champion);
                }else if(i < 10) {
                    leftTeamPicks.add(champion);
                } else if (i < 15) {
                    rightTeamBans.add(champion);
                } else {
                    rightTeamPicks.add(champion);
                }
            }
            lckGameRawDataModel.setLeftTeamBans(leftTeamBans);
            lckGameRawDataModel.setLeftTeamPicks(leftTeamPicks);
            lckGameRawDataModel.setRightTeamBans(rightTeamBans);
            lckGameRawDataModel.setRightTeamPicks(rightTeamPicks);
            lckGameRawDataModelList.add(lckGameRawDataModel);
            round += 1;
        }
        return lckGameRawDataModelList;
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

    private int parseLeftTeamScore(Element rawGameResultHTML) {
        return convertStringScoreToInt(rawGameResultHTML.select("h1").get(0).text());
    }

    private int parseRightTeamScore(Element rawGameResultHTML) {
        return convertStringScoreToInt(rawGameResultHTML.select("h1").get(2).text());
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

    private int convertStringScoreToInt(String score) {
        if (score.equals("WIN")) {
            return 1;
        }
        return 0;
    }
}
