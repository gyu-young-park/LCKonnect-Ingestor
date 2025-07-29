package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.service.YoutubeChannelService;
import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.dto.LCKPlayListAPIRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/youtube")
public class YoutubeAPIController {
    @Autowired
    private YoutubeChannelService youtubeChannelService;

//    @GetMapping("/channel")
//    public ResponseEntity<LCKChannelAPIModel> getChannelInfo() {
//        return ResponseEntity.ok(youtubeChannelService.getChannelData("lck"));
//    }

    @GetMapping("/playlist")
    public ResponseEntity<LCKPlayListAPIRespDTO> getPlayList() {
        return ResponseEntity.ok(youtubeChannelService.getLCKPlayList());
    }
}
