package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule;

public interface LCKDataFilterRule<T> {
    boolean filterByRule(T data);
}
