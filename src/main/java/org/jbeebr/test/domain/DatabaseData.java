package org.jbeebr.test.domain;

import java.util.HashMap;
import java.util.Map;

public class DatabaseData {

    private Map<String, Object> values;

    public DatabaseData() {}

    public DatabaseData(Map<String, Object> values) {
        this.values = values;
    }

    public void addValue(String name, Object value) {
        if(this.values == null) {
            this.values = new HashMap<String, Object>();
        }
        this.values.put(name, value);
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

}
