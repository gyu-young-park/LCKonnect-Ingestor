package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.service.YoutubeChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getPlayList() {
        youtubeChannelService.getLCKPlayList();
        return ResponseEntity.ok("hello");
    }
}
