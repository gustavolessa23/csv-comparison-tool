package com.gustavolessa.csvcomparisontool.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVEntry {

    protected Map<String, String> data;

    public CSVEntry() {
        this.data = new HashMap<>();
    }

    public void add(String column, String value) {
        this.data.put(column, value);
    }


    public Map<String, String> getData() {
        return this.data;
    }

    public List<String> getValues() {
        return new ArrayList<String>((data.values()));
    }

    @Override
    public String toString() {
        List<String> tmp = getValues();
        String result = "";
        for (int x = 0; x < tmp.size(); x++) {
            result = result.concat(tmp.get(x)).concat("\t\t");
        }
        return result;
    }
}
