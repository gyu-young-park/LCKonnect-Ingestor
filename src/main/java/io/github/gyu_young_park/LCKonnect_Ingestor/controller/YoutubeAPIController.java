package io.github.gyu_young_park.LCKonnect_Ingestor.controller;

import io.github.gyu_young_park.LCKonnect_Ingestor.youtube.YoutubeChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/youtube")
public class YoutubeAPIController {
    @Autowired
    private YoutubeChannelService youtubeChannelService;

    @GetMapping("/channel")
    public Mono<String> getChannelInfo() {
        return youtubeChannelService.getChannelData("UCw1DsweY9b2AKGjV4kGJP1A");
    }
}
