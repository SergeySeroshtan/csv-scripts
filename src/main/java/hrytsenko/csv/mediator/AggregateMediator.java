package hrytsenko.csv.mediator;

import hrytsenko.csv.Record;

import java.util.ArrayList;
import java.util.List;

public class AggregateMediator extends Accumulator {

    private List<Record> records;

    public AggregateMediator() {
        records = new ArrayList<Record>();
    }

    @Override
    public void mediate(Record record) {
        records.add(record);
    }

    @Override
    protected Object value() {
        return records;
    }

}
