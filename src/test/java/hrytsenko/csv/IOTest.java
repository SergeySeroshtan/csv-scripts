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
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IOTest {

    private Record recordForGoogle;
    private Record recordForOracle;

    @Before
    public void init() {
        recordForGoogle = record("ticker", "GOOG", "name", "Google", "exchange", "NASDAQ");
        recordForOracle = record("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
    }

    @Test
    public void testIO() throws IOException {
        Path temp = createTempFile(null, ".csv");
        temp.toFile().deleteOnExit();
        String path = temp.toAbsolutePath().toString();

        save(path, Arrays.asList(recordForGoogle, recordForOracle));

        List<Record> records = load(path);

        assertEquals(2, records.size());
        assertEquals("GOOG", records.get(0).getAt("ticker"));
        assertEquals("ORCL", records.get(1).getAt("ticker"));
    }

}
