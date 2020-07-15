package com.gustavolessa.csvcomparisontool.entities;

import java.util.*;

public class CSVRow {

    protected Map<String, String> data;

    public CSVRow() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;

        for (String s : this.getData().keySet()) {
            if (!((CSVRow) obj).getData().get(s).equalsIgnoreCase(this.getData().get(s)))
                return false;

        }
        return true;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getValues().toArray());
    }

}
