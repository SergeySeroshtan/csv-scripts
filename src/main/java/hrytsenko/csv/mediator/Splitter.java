package hrytsenko.csv.mediator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.util.Collection;
import java.util.List;

/**
 * Splits copies of record between mediators.
 * 
 * @author hrytsenko.anton
 */
public class Splitter extends Accumulator {

    private List<Mediator> descendants;

    /**
     * Creates empty splitter.
     */
    public Splitter() {
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
    public Splitter over(Mediator... mediators) {
        descendants = asList(mediators);
        return this;
    }

    @Override
    public void mediate(Record record) {
        for (Mediator mediator : descendants) {
            Record copy = new Record(record.content());
            mediator.mediate(copy);
        }
    }

    @Override
    public Collection<Mediator> descendants() {
        return descendants;
    }

}
