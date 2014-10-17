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
package hrytsenko.csv.groovy;

import static hrytsenko.csv.core.Records.createRecord;
import static hrytsenko.csv.groovy.Records.combine;
import static hrytsenko.csv.groovy.Records.load;
import static hrytsenko.csv.groovy.Records.map;
import static hrytsenko.csv.groovy.Records.merge;
import static hrytsenko.csv.groovy.Records.save;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordsTest {

    private Record recordForGoogle;
    private Record recordForMicrosoft;
    private Record recordForOracle;

    private String recordsCSV;

    @Before
    public void init() {
        recordForGoogle = createRecord("ticker", "GOOG", "name", "Google", "exchange", "NASDAQ");
        recordForMicrosoft = createRecord("ticker", "MSFT", "name", "Microsoft", "exchange", "NASDAQ");
        recordForOracle = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");

        recordsCSV = "ticker,name,exchange\nGOOG,Google,NASDAQ\nMSFT,Microsoft,NASDAQ\nORCL,Oracle,NYSE\n";
    }

    @Test
    public void testLoad() throws IOException {
        List<Record> records = load(new ByteArrayInputStream(recordsCSV.getBytes("UTF-8")));

        assertEquals(3, records.size());
        assertEquals("GOOG", records.get(0).getAt("ticker"));
        assertEquals("MSFT", records.get(1).getAt("ticker"));
        assertEquals("ORCL", records.get(2).getAt("ticker"));
    }

    @Test
    public void testSave() throws IOException {
        Collection<Record> records = asList(recordForGoogle, recordForMicrosoft, recordForOracle);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        save(stream, records);

        String result = new String(stream.toByteArray());
        assertEquals(recordsCSV, result);
    }

    @Test
    public void testCombine() {
        List<Record> setForNYSE = asList(recordForGoogle);
        List<Record> setForNASDAQ = asList(recordForOracle, recordForMicrosoft);

        Collection<Record> result = combine(setForNYSE, setForNASDAQ);
        assertEquals(3, result.size());
    }

    @Test
    public void testMerge() {
        List<Record> setWithName = asList(recordForGoogle, recordForOracle);

        List<Record> setWithExchange = new ArrayList<>();
        setWithExchange.add(createRecord("ticker", "GOOG", "city", "Mountain View"));
        setWithExchange.add(createRecord("ticker", "ORCL", "city", "Redwood City"));

        Collection<Record> result = merge("ticker", setWithName, setWithExchange);
        assertEquals(2, result.size());

        Record[] records = result.toArray(new Record[result.size()]);

        Record recordForGoogle = records[0];
        assertEquals(4, recordForGoogle.fields().size());
        assertEquals("Mountain View", recordForGoogle.getAt("city"));

        Record recordForOracle = records[1];
        assertEquals(4, recordForOracle.fields().size());
        assertEquals("Redwood City", recordForOracle.getAt("city"));
    }

    @Test
    public void testDistinct() {
        List<Record> set = asList(recordForGoogle, recordForOracle, recordForMicrosoft);

        Collection<String> exchanges = Records.distinct("exchange", set);
        assertArrayEquals(exchanges.toArray(), new String[] { "NASDAQ", "NYSE" });
    }

    @Test
    public void testMap() {
        List<Record> set = asList(recordForGoogle, recordForOracle);

        Map<String, Record> mappedSet = map("ticker", set);

        assertEquals(2, mappedSet.size());

        assertEquals(mappedSet.get("GOOG"), recordForGoogle);
        assertEquals(mappedSet.get("ORCL"), recordForOracle);
    }

    @Test
    public void testGroup() {
        List<Record> set = asList(recordForGoogle, recordForOracle, recordForMicrosoft);

        Map<String, List<Record>> groupedSet = Records.group("exchange", set);

        assertEquals(2, groupedSet.size());

        assertArrayEquals(new Object[] { recordForGoogle, recordForMicrosoft }, groupedSet.get("NASDAQ").toArray());
        assertArrayEquals(new Object[] { recordForOracle }, groupedSet.get("NYSE").toArray());
    }

}
