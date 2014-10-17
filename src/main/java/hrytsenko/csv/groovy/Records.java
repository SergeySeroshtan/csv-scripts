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
package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * The methods to be used in scripts to work with records.
 * 
 * @author hrytsenko.anton
 */
public final class Records {

    private Records() {
    }

    /**
     * Gets records from file.
     * 
     * @param filename
     *            the path to file to be read.
     * 
     * @return the loaded records.
     * 
     * @throws IOException
     *             if file could not be read.
     */
    public static List<Record> load(String filename) throws IOException {
        try (InputStream stream = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            return load(stream);
        }
    }

    /**
     * Gets records from stream.
     * 
     * @param stream
     *            the stream to be read.
     * 
     * @return the loaded records.
     * 
     * @throws IOException
     *             if stream could not be read.
     */
    public static List<Record> load(InputStream stream) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        ObjectReader reader = mapper.reader(Map.class).with(schema);

        Iterator<Map<String, String>> rows = reader.readValues(stream);
        List<Record> records = new ArrayList<>();
        while (rows.hasNext()) {
            Map<String, String> row = rows.next();
            Record record = new Record();
            record.putAll(row);
            records.add(record);
        }
        return records;
    }

    /**
     * Saves records into CSV file.
     * 
     * <p>
     * If file already exists, then it will be overridden.
     * 
     * @param filename
     *            the path to file to be written.
     * @param records
     *            the records to save.
     * 
     * @throws IOException
     *             if file could not be written.
     */
    public static void save(String filename, Collection<Record> records) throws IOException {
        try (OutputStream stream = Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            save(stream, records);
        }
    }

    /**
     * Saves records into stream.
     * 
     * @param stream
     *            the stream to be written.
     * @param records
     *            the records to save.
     * 
     * @throws IOException
     *             if stream could not be written.
     */
    public static void save(OutputStream stream, Collection<Record> records) throws IOException {
        Set<String> columns = new LinkedHashSet<>();
        List<Map<String, String>> rows = new ArrayList<>();
        for (Record record : records) {
            Map<String, String> values = record.values();
            columns.addAll(values.keySet());
            rows.add(values);
        }

        CsvSchema.Builder schema = CsvSchema.builder().setUseHeader(true);
        for (String column : columns) {
            schema.addColumn(column);
        }
        CsvMapper mapper = new CsvMapper();
        ObjectWriter writer = mapper.writer().withSchema(schema.build());
        writer.writeValue(stream, rows);
    }

    /**
     * Applies the given mediators to records.
     * 
     * @param records
     *            the records to be processed.
     * @param mediators
     *            the mediators to be applied.
     */
    public static void process(Collection<Record> records, Mediator... mediators) {
        for (Record record : records) {
            for (Mediator mediator : mediators) {
                mediator.mediate(record);
            }
        }
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
        List<Record> result = new ArrayList<>();
        for (Collection<Record> set : sets) {
            result.addAll(set);
        }
        return result;
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
        Map<String, Record> result = new LinkedHashMap<>();
        for (Collection<Record> set : sets) {
            for (Record record : set) {
                String key = record.getAt(field);

                Record mergedRecord = result.get(key);
                if (mergedRecord == null) {
                    mergedRecord = new Record();
                    result.put(key, mergedRecord);
                }

                mergedRecord.putAll(record.values());
            }
        }
        return result.values();
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
            values.add(record.getAt(field));
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
        Map<String, Record> result = new LinkedHashMap<>();
        for (Record record : set) {
            result.put(record.getAt(field), record);
        }
        return result;
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
        Map<String, List<Record>> result = new LinkedHashMap<>();
        for (Record record : set) {
            String key = record.getAt(field);

            List<Record> group = result.get(key);
            if (group == null) {
                group = new ArrayList<>();
                result.put(key, group);
            }

            group.add(record);
        }
        return result;
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

}
