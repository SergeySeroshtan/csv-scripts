package hrytsenko.csv.core;

/**
 * Mediators are intended for processing records.
 * 
 * @author hrytsenko.anton
 */
public interface Mediator {

    /**
     * Performs processing of record.
     * 
     * @param record
     *            the record to be processed.
     */
    void mediate(Record record);

}
