package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Aggregates all incoming records.
 * 
 * @author hrytsenko.anton
 */
public class Aggregator extends Accumulator {

    private List<Record> records;

    /**
     * Creates empty aggregator.
     */
    public Aggregator() {
        records = new ArrayList<>();
    }

    @Override
    public void mediate(Record record) {
        records.add(record);
    }

    @Override
    protected Collection<Record> value() {
        return records;
    }

}
