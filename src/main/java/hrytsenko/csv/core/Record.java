package hrytsenko.csv.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

public class Record {

    private Map<String, String> content;

    public Record() {
        this(new HashMap<String, String>());
    }

    public Record(Map<String, String> row) {
        this.content = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        for (String field : row.keySet()) {
            this.content.put(field.toLowerCase(), row.get(field));
        }
    }

    public String get(String field) {
        return content.get(field);
    }

    public void set(String field, String value) {
        content.put(field.toLowerCase(), value);
    }

    public Collection<String> fields() {
        return new LinkedHashSet<String>(content.keySet());
    }

    public Map<String, String> content() {
        return new LinkedHashMap<String, String>(content);
    }

}