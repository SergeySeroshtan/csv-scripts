/*
 * #%L
 * csv-scripts
 * %%
 * Copyright (C) 2014 Anton Hrytsenko
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hrytsenko.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Methods for processing records.
 * 
 * @author hrytsenko.anton
 */
public final class Records {

    private Records() {
    }

    /**
     * Combines records from one or more sets,
     * 
     * @param sets
     *            the sets of records to be combined.
     * 
     * @return the resulting set of records.
     */
    @SafeVarargs
    public static Collection<Record> combine(Collection<Record>... sets) {
        List<Record> combinedSet = new ArrayList<>();
        for (Collection<Record> set : sets) {
            combinedSet.addAll(set);
        }
        return combinedSet;
    }

    /**
     * Merges records from one or more sets.
     * 
     * @param field
     *            the field to be used as unique key.
     * @param sets
     *            the sets of records to be merged.
     * 
     * @return the resulting set of records.
     */
    @SafeVarargs
    public static Collection<Record> merge(String field, Collection<Record>... sets) {
        Map<String, Record> mergedSet = new LinkedHashMap<>();
        for (Collection<Record> set : sets) {
            for (Record record : set) {
                validateContainsKey(record, field);
                String key = record.getAt(field);

                Record mergedRecord = mergedSet.get(key);
                if (mergedRecord == null) {
                    mergedRecord = new Record();
                    mergedSet.put(key, mergedRecord);
                }

                mergedRecord.putAll(record.values());
            }
        }
        return new ArrayList<>(mergedSet.values());
    }

    /**
     * Gets distinct values of field.
     * 
     * @param field
     *            the name of field.
     * @param set
     *            the distinct values of field.
     * 
     * @return the distinct values of field.
     */
    public static Collection<String> distinct(String field, Collection<Record> set) {
        Set<String> values = new LinkedHashSet<>();
        for (Record record : set) {
            validateContainsKey(record, field);
            String key = record.getAt(field);

            values.add(key);
        }
        return values;
    }

    /**
     * Get mapping of records with the specified unique key.
     * 
     * <p>
     * If several records has the same value of key, then the last of them will be added to mapping.
     * 
     * @param field
     *            the field to be used as unique key.
     * @param set
     *            the set of records for mapping.
     * 
     * @return the mapped records.
     */
    public static Map<String, Record> map(String field, Collection<Record> set) {
        Map<String, Record> mappedSet = new LinkedHashMap<>();
        for (Record record : set) {
            validateContainsKey(record, field);
            String key = record.getAt(field);

            mappedSet.put(key, record);
        }
        return mappedSet;
    }

    /**
     * Splits records into groups with the specified key.
     * 
     * @param field
     *            the field to be used as key.
     * @param set
     *            the set of records for grouping.
     * 
     * @return the grouped records.
     */
    public static Map<String, List<Record>> group(String field, Collection<Record> set) {
        Map<String, List<Record>> groupedSet = new LinkedHashMap<>();
        for (Record record : set) {
            validateContainsKey(record, field);
            String key = record.getAt(field);

            List<Record> group = groupedSet.get(key);
            if (group == null) {
                group = new ArrayList<>();
                groupedSet.put(key, group);
            }

            group.add(record);
        }
        return groupedSet;
    }

    /**
     * Creates record with given values.
     * 
     * @param values
     *            the values to be added into record.
     * 
     * @return the created record.
     */
    public static Record record(Map<String, ?> values) {
        Record record = new Record();
        record.putAll(values);
        return record;
    }

    /**
     * Creates record with given content.
     * 
     * @param content
     *            the list of fields and their values to be added to record.
     * 
     * @return the created record with given fields and values.
     */
    public static Record record(String... content) {
        Record record = new Record();
        for (int i = 0; i < content.length; i += 2) {
            record.putAt(content[i], content[i + 1]);
        }
        return record;
    }

    private static void validateContainsKey(Record record, String field) {
        if (!record.contains(field)) {
            throw new IllegalArgumentException("One or more record has no key.");
        }
    }

}
