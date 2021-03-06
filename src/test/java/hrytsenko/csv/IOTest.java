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

import static hrytsenko.csv.IO.getCharset;
import static hrytsenko.csv.IO.getPath;
import static hrytsenko.csv.IO.getSchema;
import static hrytsenko.csv.IO.load;
import static hrytsenko.csv.IO.save;
import static hrytsenko.csv.Records.record;
import static hrytsenko.csv.TempFiles.createTempFile;
import static hrytsenko.csv.TempFiles.readTempFile;
import static hrytsenko.csv.TempFiles.writeTempFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Tests of methods for input/output.
 * 
 * <p>
 * These tests use temporary files, see {@link TempFiles}.
 * 
 * @author hrytsenko.anton
 */
public class IOTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetPathNotDefined() {
        getPath(asArgs());
    }

    @Test
    public void testGetCharsetDefault() {
        assertEquals(UTF_8, getCharset(asArgs()));
    }

    @Test
    public void testGetSchemaDefault() {
        CsvSchema schema = getSchema(asArgs()).build();
        assertEquals(',', schema.getColumnSeparator());
        assertEquals('"', schema.getQuoteChar());
    }

    @Test
    public void testGetSchema() {
        CsvSchema schema = getSchema(asArgs("separator", "\t", "qualifier", "`")).build();
        assertEquals('\t', schema.getColumnSeparator());
        assertEquals('`', schema.getQuoteChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaSeparatorEmpty() {
        getSchema(asArgs("separator", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaSeparatorInvalid() {
        getSchema(asArgs("separator", ", "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaQualifierEmpty() {
        getSchema(asArgs("qualifier", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaQualifierInvalid() {
        getSchema(asArgs("qualifier", "//"));
    }

    @Test
    public void testSave() throws IOException {
        List<Record> records = asList(record("ticker", "GOOG", "name", "Google"),
                record("ticker", "ORCL", "name", "Oracle"));

        String tempFilePath = createTempFile();
        save(asArgs("path", tempFilePath, "records", records));

        String tempFileData = readTempFile(tempFilePath, UTF_8);

        assertEquals("ticker,name\nGOOG,Google\nORCL,Oracle\n", tempFileData);
    }

    @Test
    public void testSaveNothing() throws IOException {
        List<Record> records = emptyList();

        String tempFilePath = createTempFile();
        save(asArgs("path", tempFilePath, "records", records));

        String tempFileData = readTempFile(tempFilePath, UTF_8);

        assertEquals(EMPTY, tempFileData);
    }

    @Test
    public void testLoad() throws IOException {
        String tempFilePath = createTempFile();
        String tempFileData = "ticker\tname\nGOOG\tGoogle\nORCL\tOracle\n";

        writeTempFile(tempFilePath, tempFileData, UTF_8);

        List<Record> records = load(asArgs("path", tempFilePath, "separator", "\t"));

        assertEquals(2, records.size());
        assertEquals("GOOG", records.get(0).getAt("ticker"));
        assertEquals("ORCL", records.get(1).getAt("ticker"));
    }

    private static Map<String, ?> asArgs(Object... args) {
        Map<String, Object> mappedArgs = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            mappedArgs.put((String) args[i], args[i + 1]);
        }
        return mappedArgs;
    }

}
