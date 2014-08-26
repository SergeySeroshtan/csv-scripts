package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;

import java.util.Collection;
import java.util.Collections;

/**
 * Accumulators are intended for collecting data during processing of records.
 * 
 * Also accumulator may just provide access to other accumulators instead of collecting data.
 * 
 * <p>
 * Accumulated data can be accessed through the assigned name.
 * 
 * @author hrytsenko.anton
 */
public abstract class Accumulator implements Mediator {

    private String name;

    /**
     * Assigns name for accumulated data.
     * 
     * @param name
     *            the name to access data.
     * 
     * @return this mediator for chaining.
     */
    public Mediator into(String name) {
        this.name = name;
        return this;
    }

    /**
     * Pulls accumulated data bound to specified name.
     * 
     * @param name
     *            the name of accumulated data.
     * 
     * @return the accumulated data or <code>null</code> if data not found.
     */
    @SuppressWarnings("unchecked")
    public <T> T pull(String name) {
        if (name == null) {
            return null;
        }

        if (name.equals(this.name)) {
            return (T) value();
        }

        Collection<Mediator> children = descendants();
        for (Mediator child : children) {
            if (child instanceof Accumulator) {
                T value = ((Accumulator) child).pull(name);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    /**
     * Returns the accumulated value. By default returns <code>null</code>.
     * 
     * @return the accumulated data.
     */
    protected Object value() {
        return null;
    }

    /**
     * Returns set of dependent mediators, that can be used for search of accumulated data.
     * 
     * @return the set of mediators.
     */
    protected Collection<Mediator> descendants() {
        return Collections.emptyList();
    }

}
