package hrytsenko.csv.mediator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import hrytsenko.csv.core.Holder;
import hrytsenko.csv.core.Mediator;

import java.util.Collection;
import java.util.List;

/**
 * Container is intended to combining other mediators.
 * 
 * @author hrytsenko.anton
 */
public abstract class Container implements Mediator, Holder {

    private List<Mediator> descendants;

    /**
     * Creates an empty container.
     */
    protected Container() {
        descendants = emptyList();
    }

    /**
     * Updates the set of dependent mediators.
     * 
     * @param mediators
     *            the ordered set of mediators.
     * 
     * @return this mediator for chaining.
     */
    public Mediator over(Mediator... mediators) {
        descendants = asList(mediators);
        return this;
    }

    @Override
    public <T> T getAt(String name) {
        if (name == null) {
            return null;
        }

        Collection<Mediator> mediators = descendants();
        for (Mediator mediator : mediators) {
            if (mediator instanceof Holder) {
                T value = ((Holder) mediator).getAt(name);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    /**
     * Returns the set of dependent mediators.
     * 
     * @return the ordered set of mediators.
     */
    protected Collection<Mediator> descendants() {
        return descendants;
    }

}
