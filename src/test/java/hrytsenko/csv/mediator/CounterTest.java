package hrytsenko.csv.mediator;

import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import org.junit.Test;

public class CounterTest {

    private static final int RECORDS_NUM = 3;
    private static final String NAME = "count";

    @Test
    public void test() {
        Counter counter = new Counter();
        counter.into(NAME);

        for (int i = 0; i < RECORDS_NUM; ++i) {
            counter.mediate(new Record());
        }

        assertEquals(RECORDS_NUM, counter.pull(NAME));
    }

}
