package hrytsenko.csv.mediator;

import hrytsenko.csv.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Aggregator extends Accumulator {

    private List<Record> records;

    public Aggregator() {
        records = new ArrayList<Record>();
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
