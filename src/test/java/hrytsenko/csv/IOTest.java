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

import static hrytsenko.csv.IO.load;
import static hrytsenko.csv.IO.save;
import static hrytsenko.csv.Records.record;
import static java.nio.file.Files.createTempFile;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class IOTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private String tempFilePath;

    @Before
    public void init() throws IOException {
        Path tempFile = createTempFile(null, ".csv");
        tempFile.toFile().deleteOnExit();
        tempFilePath = tempFile.toAbsolutePath().toString();
    }

    @Test
    public void testSave() throws IOException {
        List<Record> records = asList(record("ticker", "GOOG", "name", "Google"),
                record("ticker", "ORCL", "name", "Oracle"));

        save(asArgs("path", tempFilePath, "records", records));

        String data = readTempFile(UTF_8);

        assertEquals("ticker,name\nGOOG,Google\nORCL,Oracle\n", data);
    }

    @Test
    public void testLoad() throws IOException {
        String data = "ticker\tname\nGOOG\tGoogle\nORCL\tOracle\n";

        writeTempFile(data, UTF_8);

        List<Record> records = load(asArgs("path", tempFilePath, "fieldSeparator", "\t"));

        assertEquals(2, records.size());
        assertEquals("GOOG", records.get(0).getAt("ticker"));
        assertEquals("ORCL", records.get(1).getAt("ticker"));
    }

    private void writeTempFile(String data, Charset charset) throws IOException {
        try (BufferedWriter dataWriter = Files.newBufferedWriter(Paths.get(tempFilePath), charset,
                StandardOpenOption.WRITE)) {
            dataWriter.append(data);
        }
    }

    private String readTempFile(Charset charset) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(tempFilePath), charset)) {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
            return data.toString();
        }
    }

    private static Map<String, ?> asArgs(Object... args) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i += 2) {
            result.put((String) args[i], args[i + 1]);
        }
        return result;
    }

}
