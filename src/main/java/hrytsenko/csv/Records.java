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

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * Methods for processing records.
 * 
 * @author hrytsenko.anton
 */
public final class Records {

    private Records() {
    }

    /**
     * Gets distinct values of field.
     * 
     * @param field
     *            the name of field.
     * @param set
     *            the set of records.
     * 
     * @return the distinct values of field.
     */
    public static List<String> distinct(String field, Collection<Record> set) {
        Set<String> values = new LinkedHashSet<>();
        for (Record record : set) {
            validateContainsKey(record, field);
            String key = record.getAt(field);

            values.add(key);
        }
        return new ArrayList<>(values);
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
     * Merges records by key using closure.
     * 
     * @param field
     *            the field to be used as unique key.
     * @param set
     *            the original set of records.
     * @param otherSet
     *            the other set with records to be merged.
     * @param closure
     *            the closure that merges records.
     * 
     * @return the resulting set.
     */
    public static List<Record> merge(String field, Collection<Record> set, Collection<Record> otherSet,
            Closure<Record> closure) {
        List<Record> resultSet = new ArrayList<>();
        Map<String, Record> mergedSet = map(field, otherSet);
        for (Record record : set) {
            validateContainsKey(record, field);

            String key = record.getAt(field);
            Record mergedRecord = mergedSet.get(key);

            Record resultRecord = closure.call(record, mergedRecord);
            resultSet.add(resultRecord);
        }
        return resultSet;
    }

    /**
     * Merges records by key using {@link Record#merge(Record)}.
     * 
     * @param field
     *            the field to be used as unique key.
     * @param set
     *            the original set of records.
     * @param otherSet
     *            the set to be merged.
     * 
     * @return the resulting set.
     */
    public static List<Record> merge(String field, Collection<Record> set, Collection<Record> otherSet) {
        return merge(field, set, otherSet, new Closure<Record>(Records.class) {

            private static final long serialVersionUID = 1L;

            @Override
            public Record call(Object... args) {
                return (Record) InvokerHelper.invokeMethod(args[0], "merge", args[1]);
            }

        });
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
