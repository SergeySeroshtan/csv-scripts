package hrytsenko.csv.mediator;

import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import org.junit.Before;
import org.junit.Test;

public class CounterTest {

    private static final int RECORDS_NUM = 3;
    private static final String NAME = "total";

    private Counter counter;

    @Before
    public void init() {
        counter = new Counter();
        counter.into(NAME);
    }

    @Test
    public void test() {

        for (int i = 0; i < RECORDS_NUM; ++i) {
            counter.mediate(new Record());
        }

        assertEquals(RECORDS_NUM, counter.getAt(NAME));
    }

}
