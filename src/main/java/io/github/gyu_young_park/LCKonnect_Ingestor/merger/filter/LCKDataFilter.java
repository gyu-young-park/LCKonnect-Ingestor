package io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter;

import io.github.gyu_young_park.LCKonnect_Ingestor.merger.filter.rule.LCKDataFilterRule;

import java.util.ArrayList;
import java.util.List;

public class LCKDataFilter<T> {
    final private List<LCKDataFilterRule<T>> rules = new ArrayList<>();

    public List<T> filter(List<T> data) {
        for(var rule: rules) {
            data = data.stream().filter(rule::filterByRule).toList();
        }
        return data;
    }

    public void addRules(LCKDataFilterRule<T> rule) {
        rules.add(rule);
    }
}
