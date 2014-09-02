package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Record;

/**
 * Counts number of records.
 * 
 * @author hrytsenko.anton
 */
public class Counter extends Accumulator {

    private int count;

    /**
     * Creates counter with <tt>0</tt> value.
     */
    public Counter() {
    }

    @Override
    public void mediate(Record record) {
        ++count;
    }

    @Override
    protected Object value() {
        return count;
    }

}
