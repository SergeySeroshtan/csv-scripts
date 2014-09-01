package hrytsenko.csv.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Record contains set of fields and their values.
 * 
 * <p>
 * The names of fields are considered as case insensitive. All names are stored in lower-case.
 * 
 * @author hrytsenko.anton
 */
public final class Record {

    private Map<String, String> content;

    /**
     * Creates an empty record.
     */
    public Record() {
        this(new HashMap<String, String>());
    }

    /**
     * Creates the record with specified content.
     * 
     * @param originalContent
     *            the initial content of record.
     */
    public Record(Map<String, String> originalContent) {
        content = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : originalContent.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns value of field.
     * 
     * @param field
     *            the name of field.
     * 
     * @return the value of field.
     */
    public String get(String field) {
        return content.get(field.toLowerCase());
    }

    /**
     * Overloading of operator <tt>[]</tt> in Groovy.
     * 
     * @param field
     *            the name of field.
     * 
     * @return the value of field.
     */
    public String getAt(String field) {
        return get(field);
    }

    /**
     * Sets the value of field.
     * 
     * @param field
     *            the name of field.
     * @param value
     *            the new value for field.
     */
    public void put(String field, String value) {
        content.put(field.toLowerCase(), value);
    }

    /**
     * Overloading of operator <tt>[]</tt> in Groovy.
     * 
     * @param field
     *            the name of field.
     * @param value
     *            the new value for field.
     */
    public void putAt(String field, String value) {
        put(field, value);
    }

    /**
     * Returns the names of all fields.
     * 
     * @return the names of fields.
     */
    public Collection<String> fields() {
        return new LinkedHashSet<>(content.keySet());
    }

    /**
     * Returns the content.
     * 
     * @return the content.
     */
    public Map<String, String> content() {
        return new LinkedHashMap<>(content);
    }

}
