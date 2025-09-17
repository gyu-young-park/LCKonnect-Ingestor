package io.github.gyu_young_park.LCKonnect_Ingestor.merger.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gyu_young_park.LCKonnect_Ingestor.config.TransformConfiguration;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKCrawlAndYoutubeMapModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component("lckCrawlAndYoutubeJsonMapper")
public class LCKCrawlAndYoutubeJsonMapper implements LCKCrawlAndYoutubeMapper {
    final private Logger LOGGER = LoggerFactory.getLogger(LCKCrawlAndYoutubeJsonMapper.class);
    final private TransformConfiguration transformConfiguration;

    @Override
    public List<LCKCrawlAndYoutubeMapModel> get() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    transformConfiguration.getMapDataPath(),
                    new TypeReference<>() {}
            );
        }catch (JsonProcessingException e) {
            LOGGER.error("failed to get map data: " + transformConfiguration.getMapDataPath());
            throw new NoSuchElementException(e);
        }
    }
}
