package hrytsenko.csv.core;

import static hrytsenko.csv.core.Records.assertFields;
import static hrytsenko.csv.core.Records.createRecord;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTest {

    @Test
    public void testCreate() {
        Map<String, String> content = new LinkedHashMap<>();
        content.put("ticker", "ORCL");
        content.put("name", "Oracle");

        Record record = new Record(content);
        assertFields(record, "ticker", "name");
    }

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
    public void testRemove() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertFields(record, "ticker", "name", "exchange");

        record.remove("name");
        assertFields(record, "ticker", "exchange");
    }

    @Test
    public void testRetain() {
        Record record = createRecord("ticker", "ORCL", "name", "Oracle", "exchange", "NYSE");
        assertFields(record, "ticker", "name", "exchange");

        record.retain("ticker", "exchange");
        assertFields(record, "ticker", "exchange");
    }

}
