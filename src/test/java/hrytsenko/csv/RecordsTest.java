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

import static hrytsenko.csv.Records.distinct;
import static hrytsenko.csv.Records.group;
import static hrytsenko.csv.Records.map;
import static hrytsenko.csv.Records.merge;
import static hrytsenko.csv.Records.record;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for methods for processing records.
 * 
 * @author hrytsenko.anton
 */
public class RecordsTest {

    private Record recordForGoogle;
    private Record recordForMicrosoft;
    private Record recordForOracle;

    @Before
    public void init() {
        recordForGoogle = record("ticker", "GOOG", "name", "Google", "exchange", "NASDAQ");
        recordForMicrosoft = record("ticker", "MSFT", "name", "Microsoft", "exchange", "NASDAQ");
        recordForOracle = record("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
    }

    @Test
    public void testMerge() {
        List<Record> setWithName = asList(record("ticker", "GOOG", "name", "Google"),
                record("ticker", "ORCL", "name", "Oracle"));

        List<Record> setWithExchange = asList(record("ticker", "GOOG", "exchange", "NASDAQ"),
                record("ticker", "ORCL", "exchange", "NYSE"));

        Collection<Record> mergedSet = merge("ticker", setWithName, setWithExchange);
        assertEquals(2, mergedSet.size());

        Record[] mergedRecords = mergedSet.toArray(new Record[mergedSet.size()]);

        Record recordForGoogle = mergedRecords[0];
        assertEquals(3, recordForGoogle.fields().size());
        assertEquals("Google", recordForGoogle.getAt("name"));
        assertEquals("NASDAQ", recordForGoogle.getAt("exchange"));

        Record recordForOracle = mergedRecords[1];
        assertEquals(3, recordForOracle.fields().size());
        assertEquals("Oracle", recordForOracle.getAt("name"));
        assertEquals("NYSE", recordForOracle.getAt("exchange"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeKeyNotFound() {
        merge("symbol", asList(recordForGoogle), asList(recordForOracle));
    }

    @Test
    public void testDistinct() {
        List<Record> set = asList(recordForGoogle, recordForOracle, recordForMicrosoft);

        Collection<String> exchanges = distinct("exchange", set);
        assertArrayEquals(exchanges.toArray(), new String[] { "NASDAQ", "NYSE" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistictKeyNotFound() {
        distinct("symbol", asList(recordForGoogle, recordForOracle));
    }

    @Test
    public void testMap() {
        List<Record> set = asList(recordForGoogle, recordForOracle);

        Map<String, Record> mappedSet = map("ticker", set);

        assertEquals(2, mappedSet.size());

        assertEquals(mappedSet.get("GOOG"), recordForGoogle);
        assertEquals(mappedSet.get("ORCL"), recordForOracle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapKeyNotFound() {
        map("symbol", asList(recordForGoogle, recordForOracle));
    }

    @Test
    public void testGroup() {
        List<Record> set = asList(recordForGoogle, recordForOracle, recordForMicrosoft);

        Map<String, List<Record>> groupedSet = group("exchange", set);

        assertEquals(2, groupedSet.size());

        assertArrayEquals(new Object[] { recordForGoogle, recordForMicrosoft }, groupedSet.get("NASDAQ").toArray());
        assertArrayEquals(new Object[] { recordForOracle }, groupedSet.get("NYSE").toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupKeyNotFound() {
        group("symbol", asList(recordForGoogle, recordForOracle));
    }

}
