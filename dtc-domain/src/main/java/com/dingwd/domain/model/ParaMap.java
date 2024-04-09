package com.dingwd.domain.model;

import java.util.HashMap;
import java.util.Map;

public enum ParaMap {
    INSTANCE;

    private Map<String, Object> parameters;

    ParaMap() {
        parameters = new HashMap<>();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
