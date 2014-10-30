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
 * Methods to input/output from files.
 * 
 * @author hrytsenko.anton
 */
public final class IO {

    private IO() {
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
    }

}
