package hrytsenko.csv.mediator;

import hrytsenko.csv.Condition;
import hrytsenko.csv.Mediator;
import hrytsenko.csv.Record;

import java.util.Collection;

public class Filter extends Accumulator {

    private Condition condition;
    private Sequence branch;

    public Filter(Condition condition) {
        this.condition = condition;
    }

    public Mediator then(Mediator... mediators) {
        branch = new Sequence(mediators);
        return this;
    }

    @Override
    public void mediate(Record record) {
        if (condition.check(record)) {
            branch.mediate(record);
        }
    }

    @Override
    protected Collection<Mediator> descendants() {
        return branch.descendants();
    }

}
