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
package hrytsenko.csv.core;

import static hrytsenko.csv.core.Records.assertFields;
import static hrytsenko.csv.core.Records.createRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTest {

    private Record recordForOracle;

    @Before
    public void init() {
        recordForOracle = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
    }

    @Test
    public void testGet() {
        assertEquals("NYSE", recordForOracle.getAt("exchange"));
        assertEquals("NYSE", recordForOracle.getAt("EXCHANGE"));

        assertEquals("ORCL", recordForOracle.getProperty("ticker"));
    }

    @Test
    public void testPut() {
        assertEquals("NYSE", recordForOracle.getAt("exchange"));

        recordForOracle.putAt("exchange", "NASDAQ");
        assertEquals("NASDAQ", recordForOracle.getAt("exchange"));

        recordForOracle.setProperty("name", "Oracle Corporation");
        assertEquals("Oracle Corporation", recordForOracle.getProperty("name"));

        recordForOracle.putAt("exchange", null);
        assertEquals("null", recordForOracle.getAt("exchange"));
    }

    @Test
    public void testPutAll() {
        Map<String, String> content = new LinkedHashMap<>();
        content.put("ticker", "ORCL");
        content.put("name", "Oracle");

        Record record = new Record();
        record.putAll(content);
        assertFields(record, "ticker", "name");
    }

    @Test
    public void testRemove() {
        recordForOracle.remove("name");
        assertFields(recordForOracle, "ticker", "exchange");
    }

    @Test
    public void testRename() {
        recordForOracle.rename("ticker", "symbol");
        assertFields(recordForOracle, "symbol", "name", "exchange");
    }

    @Test
    public void testRetain() {
        recordForOracle.retain("ticker", "exchange");
        assertFields(recordForOracle, "ticker", "exchange");
    }

    @Test
    public void testCopy() {
        Record copiedRecord = recordForOracle.copy();

        assertNotSame(recordForOracle, copiedRecord);
        assertEquals(recordForOracle.values(), copiedRecord.values());
    }

}
