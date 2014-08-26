package hrytsenko.csv.core;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

public class RecordTest {

    private static final String TICKER_FIELD = "Ticker";
    private static final String TICKER_VALUE = "ORCL";

    private static final String NAME_FIELD = "Name";
    private static final String NAME_VALUE = "Oracle";

    @Test
    public void test() {
        Record record = new Record(singletonMap(TICKER_FIELD, TICKER_VALUE));

        assertEquals(TICKER_VALUE, record.get(TICKER_FIELD));
        assertEquals(TICKER_VALUE, record.get(TICKER_FIELD.toLowerCase()));
        assertEquals(TICKER_VALUE, record.get(TICKER_FIELD.toUpperCase()));

        record.set(NAME_FIELD, NAME_VALUE);
        assertEquals(NAME_VALUE, record.get(NAME_FIELD));

        Collection<String> fields = record.fields();
        assertArrayEquals(new String[] { NAME_FIELD.toLowerCase(), TICKER_FIELD.toLowerCase() }, fields.toArray());
    }

}
