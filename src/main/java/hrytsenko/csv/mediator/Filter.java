package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.util.Collection;

/**
 * Applies other mediators to all records that meet specified condition.
 * 
 * @author hrytsenko.anton
 */
public class Filter extends Accumulator {

    private Condition condition;
    private Sequence branch;

    /**
     * Creates filter, that ignores all records.
     */
    public Filter() {
        condition = new Condition.IgnoreAll();
        branch = new Sequence();
    }

    /**
     * Sets the condition that will be checked to filter records.
     * 
     * @param condition
     *            the condition to filter records.
     * 
     * @return this mediator for chaining.
     */
    public Filter when(Condition condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Sets the mediators to be applied to records that meet condition.
     * 
     * @param mediators
     *            the ordered set of mediators.
     * 
     * @return this mediator for chaining.
     */
    public Filter then(Mediator... mediators) {
        branch = new Sequence().of(mediators);
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
