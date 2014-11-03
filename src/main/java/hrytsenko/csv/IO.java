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
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
 * Methods for input/output.
 * 
 * <p>
 * Supported named arguments:
 * <dl>
 * <dt>path</dt>
 * <dd>Path to file.</dd>
 * <dt>records</dt>
 * <dd>The list of records to be saved.</dd>
 * <dt>charset</dt>
 * <dd>Charset for file, see standard charsets in {@link Charset}, default: UTF-8.</dd>
 * <dt>fieldSeparator</dt>
 * <dd>Separator for values of fields, default: comma (',').</dd>
 * <dt>fieldQualifier</dt>
 * <dd>Qualifier for values of fields, default: double-quote ('"').</dd>
 * </dl>
 * 
 * @author hrytsenko.anton
 */
public final class IO {

    private IO() {
    }

    /**
     * Gets records from file.
     * 
     * @param args
     *            the named arguments {@link IO}.
     * 
     * @return the loaded records.
     * 
     * @throws IOException
     *             if file could not be read.
     */
    public static List<Record> load(Map<String, ?> args) throws IOException {
        try (Reader dataReader = Files.newBufferedReader(getPath(args), getCharset(args))) {
            CsvSchema schema = getSchema(args).setUseHeader(true).build();
            CsvMapper mapper = new CsvMapper();
            ObjectReader reader = mapper.reader(Map.class).with(schema);

            Iterator<Map<String, String>> rows = reader.readValues(dataReader);
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
     * @param args
     *            the named arguments {@link IO}.
     * 
     * @throws IOException
     *             if file could not be written.
     */
    public static void save(Map<String, ?> args) throws IOException {
        @SuppressWarnings("unchecked")
        List<Record> records = (List<Record>) args.get("records");

        try (Writer dataWriter = Files.newBufferedWriter(getPath(args), getCharset(args), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Set<String> columns = new LinkedHashSet<>();
            List<Map<String, String>> rows = new ArrayList<>();
            for (Record record : records) {
                Map<String, String> values = record.values();
                columns.addAll(values.keySet());
                rows.add(values);
            }

            CsvSchema.Builder schemaBuilder = getSchema(args).setUseHeader(true);
            for (String column : columns) {
                schemaBuilder.addColumn(column);
            }
            CsvMapper mapper = new CsvMapper();
            ObjectWriter writer = mapper.writer().withSchema(schemaBuilder.build());
            writer.writeValue(dataWriter, rows);
        }
    }

    private static Path getPath(Map<String, ?> args) {
        String path = (String) args.get("path");
        return Paths.get(path);
    }

    private static Charset getCharset(Map<String, ?> args) {
        String charsetName = (String) args.get("charset");
        if (charsetName == null) {
            charsetName = "UTF-8";
        }
        return Charset.forName(charsetName);
    }

    private static CsvSchema.Builder getSchema(Map<String, ?> args) {
        String fieldSeparator = (String) args.get("fieldSeparator");
        if (fieldSeparator == null) {
            fieldSeparator = ",";
        }
        if (fieldSeparator.length() > 1) {
            throw new IllegalArgumentException("Use single character as separator for fields.");
        }

        String fieldQualifier = (String) args.get("fieldQualifier");
        if (fieldQualifier == null) {
            fieldQualifier = "\"";
        }
        if (fieldQualifier.length() > 1) {
            throw new IllegalArgumentException("Use single character as qualifier for fields.");
        }

        CsvSchema.Builder builder = CsvSchema.builder();
        builder.setColumnSeparator(fieldSeparator.charAt(0));
        builder.setQuoteChar(fieldQualifier.charAt(0));
        return builder;
    }

}
