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

import org.junit.Test;

public class RecordTest {

    @Test
    public void testGet() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertEquals("NYSE", record.getAt("Exchange"));
    }

    @Test
    public void testPut() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertEquals("NYSE", record.getAt("exchange"));

        record.putAt("exchange", "NASDAQ");
        assertEquals("NASDAQ", record.getAt("exchange"));
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
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertFields(record, "ticker", "name", "exchange");

        record.remove("name");
        assertFields(record, "ticker", "exchange");
    }

    @Test
    public void testRename() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle");
        assertFields(record, "ticker", "name");

        record.rename("ticker", "symbol");
        assertFields(record, "symbol", "name");
    }

    @Test
    public void testRetain() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertFields(record, "ticker", "name", "exchange");

        record.retain("ticker", "exchange");
        assertFields(record, "ticker", "exchange");
    }

    @Test
    public void testCopy() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        Record copy = record.copy();

        assertNotSame(record, copy);
        assertEquals(record.values(), copy.values());
    }

}
