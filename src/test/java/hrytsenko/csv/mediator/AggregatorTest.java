package hrytsenko.csv.mediator;

import static org.junit.Assert.assertArrayEquals;
import hrytsenko.csv.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AggregatorTest {

    private static final int RECORDS_NUM = 3;
    private static final String NAME = "records";

    private Aggregator aggregator;
    private List<Record> records;

    @Before
    public void init() {
        aggregator = new Aggregator();
        aggregator.into(NAME);

        records = new ArrayList<Record>();
        for (int i = 0; i < RECORDS_NUM; ++i) {
            records.add(new Record());
        }
    }

    @Test
    public void test() {
        for (Record record : records) {
            aggregator.mediate(record);
        }

        Collection<Record> aggregated = aggregator.valueOf(NAME);
        assertArrayEquals(records.toArray(), aggregated.toArray());
    }

}
