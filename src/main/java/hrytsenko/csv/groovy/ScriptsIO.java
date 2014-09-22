/**
 * Copyright (C) 2014 Anton Hrytsenko (hrytsenko.anton@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Contains methods for input/output.
 * 
 * @author hrytsenko.anton
 */
public final class ScriptsIO {

    public static final char DEFAULT_ESCAPE_CHAR = '"';

    private ScriptsIO() {
    }

    /**
     * Reads the set of records from CSV file.
     * 
     * @param filename
     *            the name of file to be read.
     * 
     * @return the ordered set of records.
     * 
     * @throws IOException
     *             if file could not be read.
     */
    public static Collection<Record> load(String filename) throws IOException {
        try (InputStream stream = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            CsvSchema schema = CsvSchema.emptySchema().withHeader().withEscapeChar(DEFAULT_ESCAPE_CHAR);
            CsvMapper mapper = new CsvMapper();
            ObjectReader reader = mapper.reader(Map.class).with(schema);

            Iterator<Map<String, String>> rows = reader.readValues(stream);
            List<Record> records = new ArrayList<>();
            while (rows.hasNext()) {
                Map<String, String> row = rows.next();
                records.add(new Record(row));
            }
            return records;
        }
    }

    /**
     * Writes the set of records into CSV file.
     * 
     * If file already exists, then it will be overridden.
     * 
     * @param filename
     *            the name of file to be written.
     * @param records
     *            the ordered set of records.
     * 
     * @throws IOException
     *             if file could not be written.
     */
    public static void save(String filename, Collection<Record> records) throws IOException {
        try (OutputStream stream = Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Set<String> columns = new LinkedHashSet<>();
            List<Map<String, String>> rows = new ArrayList<>();
            for (Record record : records) {
                Map<String, String> row = record.content();
                columns.addAll(row.keySet());
                rows.add(row);
            }

            CsvSchema.Builder schema = CsvSchema.builder().setUseHeader(true).setEscapeChar(DEFAULT_ESCAPE_CHAR);
            for (String column : columns) {
                schema.addColumn(column);
            }
            CsvMapper mapper = new CsvMapper();
            ObjectWriter writer = mapper.writer().withSchema(schema.build());
            writer.writeValue(stream, rows);
        }
    }

    /**
     * Applies the set of mediators to records in CSV file.
     * 
     * @param filename
     *            the name of file to be processed.
     * @param mediators
     *            the ordered set of mediators.
     * 
     * @throws IOException
     *             if file could not be read.
     */
    public static void process(String filename, Mediator... mediators) throws IOException {
        Collection<Record> records = load(filename);
        for (Record record : records) {
            for (Mediator mediator : mediators) {
                mediator.mediate(record);
            }
        }
    }

}
