package io.github.gyu_young_park.LCKonnect_Ingestor.data.convertor;

public interface DataModelToEntity<D, E> {
    E convert(D domain);
}
