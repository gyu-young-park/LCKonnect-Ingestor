package io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor;

import io.github.gyu_young_park.LCKonnect_Ingestor.data.entity.MatchEntity;
import io.github.gyu_young_park.LCKonnect_Ingestor.data.vo.Thumbnail;
import io.github.gyu_young_park.LCKonnect_Ingestor.merger.model.LCKVideoAndInfoModel;
import org.springframework.stereotype.Component;

@Component
public class LCKVideoAndInfoModelToMatchEntityConvertor implements DataModelToEntity<LCKVideoAndInfoModel, MatchEntity> {
    @Override
    public MatchEntity convert(LCKVideoAndInfoModel lckVideoAndInfoModel) {
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
        return matchEntity;
    }
}
