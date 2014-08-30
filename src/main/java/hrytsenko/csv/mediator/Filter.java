package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Applies other mediators to all records that meet specified condition.
 * 
 * @author hrytsenko.anton
 */
public class Filter extends Container {

    private Condition condition;

    /**
     * Creates filter, that ignores all records.
     */
    public Filter() {
        condition = new Condition.IgnoreAll();
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

    @Override
    public void mediate(Record record) {
        if (!condition.check(record)) {
            return;
        }
        for (Mediator descendant : descendants()) {
            descendant.mediate(record);
        }
    }

}
