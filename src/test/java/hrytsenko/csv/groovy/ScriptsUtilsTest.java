package hrytsenko.csv.groovy;

import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScriptsUtilsTest {

    @Test
    public void testAppend() {
        Collection<Record> appendedSet = ScriptsUtils.append(
                Collections.singletonList(createRecord("ticker", "GOOG", "name", "Google")),
                Collections.singletonList(createRecord("ticker", "ORCL", "name", "Oracle")));

        assertEquals(2, appendedSet.size());
    }

    @Test
    public void testMerge() {
        List<Record> setWithName = new ArrayList<>();
        setWithName.add(createRecord("ticker", "GOOG", "name", "Google"));
        setWithName.add(createRecord("ticker", "ORCL", "name", "Oracle"));

        List<Record> setWithExchange = new ArrayList<>();
        setWithExchange.add(createRecord("ticker", "GOOG", "exchange", "NASDAQ"));
        setWithExchange.add(createRecord("ticker", "ORCL", "exchange", "NYSE"));

        Collection<Record> mergedSet = ScriptsUtils.merge("ticker", setWithName, setWithExchange);

        assertEquals(2, mergedSet.size());

        Record[] records = mergedSet.toArray(new Record[mergedSet.size()]);

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

    private static Record createRecord(String... content) {
        Record record = new Record();
        for (int i = 0; i < content.length; i += 2) {
            record.putAt(content[i], content[i + 1]);
        }
        return record;
    }

}
