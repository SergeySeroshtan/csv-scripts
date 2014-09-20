package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.mediator.Aggregator;
import hrytsenko.csv.mediator.Counter;
import hrytsenko.csv.mediator.Filter;
import hrytsenko.csv.mediator.Sequence;
import hrytsenko.csv.mediator.Splitter;

/**
 * Contains methods for processing.
 * 
 * @author hrytsenko.anton
 */
public final class ScriptsDSL {

    /**
     * Creates {@link Aggregator} for records.
     * 
     * @param name
     *            the name for resulting value.
     * 
     * @return the created mediator.
     */
    public static Aggregator aggregate(String name) {
        Aggregator aggregator = new Aggregator();
        aggregator.into(name);
        return aggregator;
    }

    /**
     * Creates {@link Counter} for records.
     * 
     * @param name
     *            the name for resulting value.
     * 
     * @return the created mediator.
     */
    public static Counter count(String name) {
        Counter counter = new Counter();
        counter.into(name);
        return counter;
    }

    /**
     * Allows to define the custom mediator in more natural form using Groovy.
     * 
     * <p>
     * I.e., you can write: <tt>def custom = apply({ rec -> ... })</tt>; instead of:
     * <tt>def custom = {rec -> ... } as Mediator</tt>.
     * 
     * @param mediator
     *            the custom mediator.
     * 
     * @return the custom mediator.
     */
    public static Mediator apply(Mediator mediator) {
        return mediator;
    }

    /**
     * Creates {@link Filter} with specified condition.
     * 
     * @param condition
     *            the custom condition.
     * 
     * @return the created mediator.
     */
    public static Filter filter(Condition condition) {
        return new Filter().when(condition);
    }

    /**
     * Creates {@link Sequence} of mediators.
     * 
     * @param mediators
     *            the set of mediators for {@link Sequence}.
     * 
     * @return the created mediator.
     */
    public static Sequence sequence(Mediator... mediators) {
        Sequence sequence = new Sequence();
        sequence.over(mediators);
        return sequence;
    }

    /**
     * Creates {@link Splitter} over set of mediators.
     * 
     * @param mediators
     *            the set of mediators for {@link Splitter}.
     * 
     * @return the created mediator.
     */
    public static Splitter split(Mediator... mediators) {
        Splitter splitter = new Splitter();
        splitter.over(mediators);
        return splitter;
    }

    private ScriptsDSL() {
    }

}
