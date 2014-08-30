package hrytsenko.csv.mediator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.util.Collection;
import java.util.List;

/**
 * Combines mediators for sequential processing of records.
 * 
 * @author hrytsenko.anton
 */
public class Sequence extends Accumulator {

    private List<Mediator> descendants;

    /**
     * Creates empty sequence.
     */
    public Sequence() {
        descendants = emptyList();
    }

    /**
     * Sets the mediators to be applied to records.
     * 
     * @param mediators
     *            the ordered set of mediators.
     * 
     * @return this mediator for chaining.
     */
    public Sequence over(Mediator... mediators) {
        descendants = asList(mediators);
        return this;
    }

    @Override
    public void mediate(Record record) {
        for (Mediator mediator : descendants) {
            mediator.mediate(record);
        }
    }

    @Override
    public Collection<Mediator> descendants() {
        return descendants;
    }

}
