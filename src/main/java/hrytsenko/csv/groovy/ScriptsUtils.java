package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Record;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains utility methods.
 * 
 * @author hrytsenko.anton
 */
public final class ScriptsUtils {

    /**
     * Merges records from one or more sets of records.
     * 
     * @param idField
     *            the name of field, which value can be used as unique identifier.
     * @param sets
     *            the sets of records to be merged.
     * 
     * @return the resulting set of records.
     */
    @SafeVarargs
    public static Collection<Record> merge(String idField, Collection<Record>... sets) {
        Map<String, Record> result = new LinkedHashMap<>();
        for (Collection<Record> set : sets) {
            for (Record record : set) {
                String id = record.get(idField);
                result.put(id, merge(record, result.get(id)));
            }
        }
        return result.values();
    }

    /**
     * Creates map of records, using value of specified field as key.
     * 
     * @param idField
     *            the name of field, which value can be used as unique identifier.
     * @param set
     *            the set of records for mapping.
     * 
     * @return the mapped set of records.
     */
    public static Map<String, Record> map(String idField, Collection<Record> set) {
        Map<String, Record> result = new LinkedHashMap<>();
        for (Record record : set) {
            result.put(record.get(idField), record);
        }
        return result;
    }

    private static Record merge(Record source, Record target) {
        if (target == null) {
            return new Record(source.content());
        }

        Record result = new Record(target.content());

        for (String field : source.fields()) {
            result.put(field, source.get(field));
        }
        return result;
    }

}
