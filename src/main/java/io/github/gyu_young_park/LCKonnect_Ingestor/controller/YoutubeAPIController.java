package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.service.YoutubeChannelService;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.model.LCKYoutubeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YoutubeAPIController {
    final private YoutubeChannelService youtubeChannelService;

//    @GetMapping("/channel")
//    public ResponseEntity<LCKChannelAPIModel> getChannelInfo() {
//        return ResponseEntity.ok(youtubeChannelService.getChannelData("lck"));
//    }

    @GetMapping("/playlist")
    public Map<String, LCKYoutubeModel> getPlayList() {
        LCKYoutubeModel lckYoutubeModel = youtubeChannelService.getLCKPlayList();
        Map<String, LCKYoutubeModel> data = new HashMap<>();
        data.put("message", lckYoutubeModel);
        return data;
    }
}
