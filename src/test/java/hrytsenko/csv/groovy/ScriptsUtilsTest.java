package hrytsenko.csv.groovy;

import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ScriptsUtilsTest {

    @Test
    public void testMerge() {
        List<Record> recordsWithName = new ArrayList<>();
        recordsWithName.add(createRecord("ticker", "GOOG", "name", "Google"));
        recordsWithName.add(createRecord("ticker", "ORCL", "name", "Oracle"));

        List<Record> recordsWithExchange = new ArrayList<>();
        recordsWithName.add(createRecord("ticker", "GOOG", "exchange", "NASDAQ"));
        recordsWithName.add(createRecord("ticker", "ORCL", "exchange", "NYSE"));

        Collection<Record> mergedRecords = ScriptsUtils.merge("ticker", recordsWithName, recordsWithExchange);

        assertEquals(2, mergedRecords.size());

        Record[] records = mergedRecords.toArray(new Record[mergedRecords.size()]);

        Record recordForGoogle = records[0];
        assertEquals(3, recordForGoogle.fields().size());
        assertEquals("GOOG", recordForGoogle.get("ticker"));
        assertEquals("Google", recordForGoogle.get("name"));
        assertEquals("NASDAQ", recordForGoogle.get("exchange"));

        Record recordForOracle = records[1];
        assertEquals(3, recordForOracle.fields().size());
        assertEquals("ORCL", recordForOracle.get("ticker"));
        assertEquals("Oracle", recordForOracle.get("name"));
        assertEquals("NYSE", recordForOracle.get("exchange"));
    }

    private static Record createRecord(String... content) {
        Record record = new Record();
        for (int i = 0; i < content.length; i += 2) {
            record.put(content[i], content[i + 1]);
        }
        return record;
    }

}
