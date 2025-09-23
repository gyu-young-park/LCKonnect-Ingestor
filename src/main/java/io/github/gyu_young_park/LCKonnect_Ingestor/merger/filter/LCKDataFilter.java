package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import java.util.List;

public interface LCKDataFilter<T> {
    List<T> filter(List<T> data);
}
