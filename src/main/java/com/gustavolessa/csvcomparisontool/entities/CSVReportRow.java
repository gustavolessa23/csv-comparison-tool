package com.gustavolessa.csvcomparisontool.entities;

import java.util.*;

public class CSVReportRow extends CSVRow {

    private final Set<String> conflicting;

    private long id;

    public CSVReportRow() {
        super();
        this.conflicting = new HashSet<>();
        id = 0;
    }

    public static CSVReportRow reportFromEntry(CSVRow entry) {
        CSVReportRow temp = new CSVReportRow();
        temp.setData(entry.getData());
        return temp;
    }

    public void add(String column, String value) {
        this.data.put(column, value);
    }

    public void addConflicting(String column) {
        this.conflicting.add(column);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<String> getConflicting() {
        return this.conflicting;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
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
