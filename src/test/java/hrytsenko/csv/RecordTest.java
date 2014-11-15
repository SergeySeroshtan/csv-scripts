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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
        assertNull(recordForOracle.getAt("Ticker"));

    }

    @Test
    public void testGetUsingProperty() {
        Record record = spy(recordForOracle);
        assertEquals("ORCL", record.getProperty("ticker"));

        verify(record).getAt("ticker");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIfFieldIsNull() {
        recordForOracle.getAt(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIfFieldIsEmpty() {
        recordForOracle.getAt("");
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

    @Test(expected = IllegalArgumentException.class)
    public void testPutIfFieldIsNull() {
        recordForOracle.putAt(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIfFieldIsEmpty() {
        recordForOracle.putAt("", "");
    }

    @Test
    public void testPutUsingProperty() {
        Record record = spy(recordForOracle);
        record.setProperty("exchange", "NASDAQ");
        assertEquals("NASDAQ", record.getAt("exchange"));

        verify(record).putAt("exchange", "NASDAQ");
    }

    @Test
    public void testPutIfValueIsNull() {
        recordForOracle.putAt("exchange", null);
        assertEquals("", recordForOracle.getAt("exchange"));
    }

    @Test
    public void testPutAll() {
        Map<String, String> content = new LinkedHashMap<>();
        content.put("ticker", "ORCL");
        content.put("name", "Oracle");

        Record record = new Record();
        record.putAll(content);
        assertFields(record, "ticker", "name");
        assertEquals("ORCL", record.getAt("ticker"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIfValuesIsNull() {
        Record record = new Record();
        record.putAll(null);
    }

    @Test
    public void testContains() {
        assertTrue(recordForOracle.contains("ticker"));
        assertFalse(recordForOracle.contains("price"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsIfFieldIsNull() {
        recordForOracle.contains(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsIfFieldIsEmpty() {
        recordForOracle.contains("");
    }

    @Test
    public void testRemove() {
        recordForOracle.remove("name");
        assertFields(recordForOracle, "ticker", "exchange");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNotFound() {
        recordForOracle.remove("price");
    }

    @Test
    public void testRename() {
        recordForOracle.rename("ticker", "symbol");
        assertFields(recordForOracle, "symbol", "name", "exchange");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenameNotFound() {
        recordForOracle.rename("symbol", "ticker");
    }

    @Test
    public void testRetain() {
        recordForOracle.retain("ticker", "exchange");
        assertFields(recordForOracle, "ticker", "exchange");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetainNotFound() {
        recordForOracle.remove("ticker", "price");
    }

    @Test
    public void testCopy() {
        Record copy = recordForOracle.copy();

        assertNotSame(recordForOracle, copy);
        assertEquals(recordForOracle.values(), copy.values());
    }

    private static void assertFields(Record record, String... expectedFields) {
        Collection<String> actualFields = record.fields();
        assertArrayEquals(expectedFields, actualFields.toArray());
    }

}
