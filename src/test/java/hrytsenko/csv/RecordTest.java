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

import static hrytsenko.csv.Records.record;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for methods of class {@link Record}.
 * 
 * @author hrytsenko.anton
 */
public class RecordTest {

    private Record recordForOracle;

    @Before
    public void init() {
        recordForOracle = record("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
    }

    @Test
    public void testGet() {
        assertEquals("ORCL", recordForOracle.getAt("ticker"));
        assertEquals("ORCL", recordForOracle.getAt("TICKER"));

    }

    @Test
    public void testGetUsingProperty() {
        assertEquals("ORCL", recordForOracle.getProperty("ticker"));
        assertEquals("ORCL", recordForOracle.getProperty("TICKER"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIfFieldNotDefined() {
        recordForOracle.getAt(null);
    }

    public void testGetIfFieldNotFound() {
        assertNull(recordForOracle.getAt("unknown"));
    }

    @Test
    public void testPut() {
        assertEquals("NYSE", recordForOracle.getAt("exchange"));

        recordForOracle.putAt("exchange", "NASDAQ");
        assertEquals("NASDAQ", recordForOracle.getAt("exchange"));
    }

    @Test
    public void testPutUsingProperty() {
        recordForOracle.setProperty("exchange", "NASDAQ");
        assertEquals("NASDAQ", recordForOracle.getAt("exchange"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIfFieldNotDefined() {
        recordForOracle.putAt(null, "any");
    }

    @Test
    public void testPutIfValueIsNull() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testPutIfValuesNotDefined() {
        Record record = new Record();
        record.putAll(null);
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

    private static void assertFields(Record record, String... expectedFields) {
        Collection<String> actualFields = record.fields();
        Assert.assertArrayEquals(expectedFields, actualFields.toArray());
    }

}
