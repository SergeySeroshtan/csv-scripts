package hrytsenko.csv.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTest {

    private static final String TICKER_FIELD = "Ticker";
    private static final String TICKER_VALUE = "ORCL";

    private static final String NAME_FIELD = "Name";
    private static final String NAME_VALUE = "Oracle";

    private static final String EXCHANGE_FIELD = "Exchange";
    private static final String EXCHANGE_VALUE = "NYSE";

    @Test
    public void testCreate() {
        Map<String, String> content = new LinkedHashMap<>();
        content.put(TICKER_FIELD, TICKER_VALUE);
        content.put(NAME_FIELD, NAME_VALUE);

        Record record = new Record(content);

        assertFields(record, TICKER_FIELD, NAME_FIELD);
    }

    @Test
    public void testModify() {
        Record record = new Record();

        record.put(TICKER_FIELD, TICKER_VALUE);

        assertEquals(TICKER_VALUE, record.get(TICKER_FIELD.toLowerCase()));
        assertEquals(TICKER_VALUE, record.get(TICKER_FIELD.toUpperCase()));
        assertFields(record, TICKER_FIELD);

        record.put(NAME_FIELD, NAME_VALUE);

        assertEquals(NAME_VALUE, record.get(NAME_FIELD));
        assertFields(record, TICKER_FIELD, NAME_FIELD);
    }

    @Test
    public void testRemove() {
        Record record = new Record();
        record.put(TICKER_FIELD, TICKER_VALUE);
        record.put(NAME_FIELD, NAME_VALUE);
        record.put(EXCHANGE_FIELD, EXCHANGE_VALUE);

        assertFields(record, TICKER_FIELD, NAME_FIELD, EXCHANGE_FIELD);

        record.remove(NAME_FIELD);

        assertFields(record, TICKER_FIELD, EXCHANGE_FIELD);
    }

    @Test
    public void testRetain() {
        Record record = new Record();
        record.put(TICKER_FIELD, TICKER_VALUE);
        record.put(NAME_FIELD, NAME_VALUE);
        record.put(EXCHANGE_FIELD, EXCHANGE_VALUE);

        assertFields(record, TICKER_FIELD, NAME_FIELD, EXCHANGE_FIELD);

        record.retain(TICKER_FIELD, EXCHANGE_FIELD);

        assertFields(record, TICKER_FIELD, EXCHANGE_FIELD);
    }

    private static void assertFields(Record record, String... expecteds) {
        Collection<String> actuals = record.fields();
        assertEquals(expecteds.length, actuals.size());
        for (String expected : expecteds) {
            assertTrue(actuals.contains(expected.toLowerCase()));
        }
    }

}
