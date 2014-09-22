/**
 * Copyright (C) 2014 Anton Hrytsenko (hrytsenko.anton@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hrytsenko.csv.groovy;

import static hrytsenko.csv.core.Records.createRecord;
import static hrytsenko.csv.groovy.ScriptsUtils.combine;
import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScriptsUtilsTest {

    @Test
    public void testCombine() {
        List<Record> setForNYSE = new ArrayList<>();
        setForNYSE.add(createRecord("ticker", "GOOG", "name", "Google"));

        List<Record> setForNASDAQ = new ArrayList<>();
        setForNASDAQ.add(createRecord("ticker", "GOOG", "name", "Google"));
        setForNASDAQ.add(createRecord("ticker", "ORCL", "name", "Oracle"));

        Collection<Record> result = combine(setForNYSE, setForNASDAQ);
        assertEquals(3, result.size());
    }

    @Test
    public void testMerge() {
        List<Record> setWithName = new ArrayList<>();
        setWithName.add(createRecord("ticker", "GOOG", "name", "Google"));
        setWithName.add(createRecord("ticker", "ORCL", "name", "Oracle"));

        List<Record> setWithExchange = new ArrayList<>();
        setWithExchange.add(createRecord("ticker", "GOOG", "exchange", "NASDAQ"));
        setWithExchange.add(createRecord("ticker", "ORCL", "exchange", "NYSE"));

        Collection<Record> result = ScriptsUtils.merge("ticker", setWithName, setWithExchange);
        assertEquals(2, result.size());

        Record[] records = result.toArray(new Record[result.size()]);

        Record recordForGoogle = records[0];
        assertEquals(3, recordForGoogle.fields().size());
        assertEquals("GOOG", recordForGoogle.getAt("ticker"));
        assertEquals("Google", recordForGoogle.getAt("name"));
        assertEquals("NASDAQ", recordForGoogle.getAt("exchange"));

        Record recordForOracle = records[1];
        assertEquals(3, recordForOracle.fields().size());
        assertEquals("ORCL", recordForOracle.getAt("ticker"));
        assertEquals("Oracle", recordForOracle.getAt("name"));
        assertEquals("NYSE", recordForOracle.getAt("exchange"));
    }

    @Test
    public void testMap() {
        List<Record> set = new ArrayList<>();
        Record recordForGoogle = createRecord("ticker", "GOOG", "name", "Google");
        set.add(recordForGoogle);
        Record recordForOracle = createRecord("ticker", "ORCL", "name", "Oracle");
        set.add(recordForOracle);

        Map<String, Record> mappedSet = ScriptsUtils.map("ticker", set);

        assertEquals(2, mappedSet.size());

        assertEquals(mappedSet.get("GOOG"), recordForGoogle);
        assertEquals(mappedSet.get("ORCL"), recordForOracle);
    }

}
