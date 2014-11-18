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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.newInputStream;
import groovy.lang.Closure;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.input.BOMInputStream;

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
 * <dt>separator</dt>
 * <dd>Separator for values of fields, default: comma (',').</dd>
 * <dt>qualifier</dt>
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
        return load(args, null);
    }

    /**
     * Gets records from file.
     * 
     * <p>
     * If closure is given, then it will be applied to each record.
     * 
     * @param args
     *            the named arguments {@link IO}.
     * @param closure
     *            the closure to be applied to each record.
     * 
     * @return the loaded records.
     * 
     * @throws IOException
     *             if file could not be read.
     */
    public static List<Record> load(Map<String, ?> args, Closure<?> closure) throws IOException {
        try (InputStream dataStream = newInputStream(getPath(args), StandardOpenOption.READ);
                InputStream bomStream = new BOMInputStream(dataStream);
                Reader dataReader = new InputStreamReader(bomStream, getCharset(args))) {
            CsvSchema csvSchema = getSchema(args).setUseHeader(true).build();
            CsvMapper csvMapper = new CsvMapper();
            ObjectReader csvReader = csvMapper.reader(Map.class).with(csvSchema);

            Iterator<Map<String, String>> rows = csvReader.readValues(dataReader);
            List<Record> records = new ArrayList<>();
            while (rows.hasNext()) {
                Map<String, String> row = rows.next();
                Record record = new Record();
                record.putAll(row);
                records.add(record);

                if (closure != null) {
                    closure.call(record);
                }
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
        Iterable<Record> records = (Iterable<Record>) args.get("records");

        try (Writer dataWriter = newBufferedWriter(getPath(args), getCharset(args), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Set<String> columns = new LinkedHashSet<>();
            List<Map<String, String>> rows = new ArrayList<>();
            for (Record record : records) {
                Map<String, String> values = record.values();
                columns.addAll(values.keySet());
                rows.add(values);
            }

            CsvSchema.Builder csvSchema = getSchema(args).setUseHeader(true);
            for (String column : columns) {
                csvSchema.addColumn(column);
            }
            CsvMapper csvMapper = new CsvMapper();
            ObjectWriter csvWriter = csvMapper.writer().withSchema(csvSchema.build());
            csvWriter.writeValue(dataWriter, rows);
        }
    }

    private static Path getPath(Map<String, ?> args) {
        CharSequence path = (CharSequence) args.get("path");
        return Paths.get(path.toString());
    }

    private static Charset getCharset(Map<String, ?> args) {
        CharSequence charsetName = (CharSequence) args.get("charset");

        return charsetName != null ? Charset.forName(charsetName.toString()) : UTF_8;
    }

    private static CsvSchema.Builder getSchema(Map<String, ?> args) {
        CharSequence separator = (CharSequence) args.get("separator");
        if (separator == null) {
            separator = ",";
        }
        if (separator.length() != 1) {
            throw new IllegalArgumentException("Use single character as separator.");
        }

        CharSequence qualifier = (CharSequence) args.get("qualifier");
        if (qualifier == null) {
            qualifier = "\"";
        }
        if (qualifier.length() != 1) {
            throw new IllegalArgumentException("Use single character as qualifier.");
        }

        CsvSchema.Builder schema = CsvSchema.builder();
        schema.setColumnSeparator(separator.charAt(0));
        schema.setQuoteChar(qualifier.charAt(0));
        return schema;
    }

}
