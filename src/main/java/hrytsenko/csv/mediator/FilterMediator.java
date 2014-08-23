package hrytsenko.csv.mediator;

import static java.util.Collections.singleton;
import hrytsenko.csv.Condition;
import hrytsenko.csv.Mediator;
import hrytsenko.csv.Record;

import java.util.Collection;

public class FilterMediator extends Accumulator {

    private Condition condition;
    private Mediator branch;

    public FilterMediator(Condition condition) {
        this.condition = condition;
    }

    public Mediator then(Mediator... mediators) {
        this.branch = new SequenceMediator(mediators);
        return this;
    }

    @Override
    public void mediate(Record record) {
        if (condition.check(record)) {
            branch.mediate(record);
        }
    }

    @Override
    protected Collection<Mediator> children() {
        return singleton(branch);
    }

}
