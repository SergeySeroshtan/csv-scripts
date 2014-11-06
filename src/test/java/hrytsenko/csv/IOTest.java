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
import static hrytsenko.csv.TempFiles.UTF_8;
import static hrytsenko.csv.TempFiles.createTempFile;
import static hrytsenko.csv.TempFiles.readTempFile;
import static hrytsenko.csv.TempFiles.writeTempFile;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests of methods for input/output.
 * 
 * <p>
 * These tests use temporary files, see {@link TempFiles}.
 * 
 * @author hrytsenko.anton
 */
public class IOTest {

    private String tempFilePath;

    @Before
    public void init() throws IOException {
        tempFilePath = createTempFile();
    }

    @Test
    public void testSave() throws IOException {
        List<Record> records = asList(record("ticker", "GOOG", "name", "Google"),
                record("ticker", "ORCL", "name", "Oracle"));

        save(asArgs("path", tempFilePath, "records", records));

        String tempFileData = readTempFile(tempFilePath, UTF_8);

        assertEquals("ticker,name\nGOOG,Google\nORCL,Oracle\n", tempFileData);
    }

    @Test
    public void testLoad() throws IOException {
        String tempFileData = "ticker\tname\nGOOG\tGoogle\nORCL\tOracle\n";

        writeTempFile(tempFilePath, tempFileData, UTF_8);

        List<Record> records = load(asArgs("path", tempFilePath, "fieldSeparator", "\t"));

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
