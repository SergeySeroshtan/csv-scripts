package hrytsenko.csv.core;

/**
 * Some mediators may collect data during processing of records.
 * 
 * This interface allow to access such data.
 * 
 * @author hrytsenko.anton
 */
public interface Holder {

    /**
     * Gets value from mediator.
     * 
     * <p>
     * Also overloads operator <tt>[]</tt> in Groovy.
     * 
     * @param name
     *            the name associated with value.
     * 
     * @return the value or <code>null</code> if nothing found.
     */
    <T> T getAt(String name);

}
