package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Holder;
import hrytsenko.csv.core.Mediator;

/**
 * Accumulator contains data bound to name.
 * 
 * Thus this data can be accessed only through this name.
 * 
 * @author hrytsenko.anton
 */
public abstract class Accumulator implements Mediator, Holder {

    private String name;

    /**
     * Assigns name for value.
     * 
     * @param name
     *            the name for value.
     * 
     * @return this mediator for chaining.
     */
    public Mediator into(String name) {
        this.name = name;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T pull(String name) {
        if (name == null) {
            return null;
        }

        return name.equals(this.name) ? (T) value() : null;
    }

    /**
     * Returns the accumulated value.
     * 
     * @return the accumulated value.
     */
    protected abstract Object value();

}
