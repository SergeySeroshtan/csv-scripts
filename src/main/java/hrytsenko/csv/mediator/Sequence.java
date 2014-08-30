package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Combines mediators for sequential processing of records.
 * 
 * @author hrytsenko.anton
 */
public class Sequence extends Container {

    /**
     * Creates empty sequence.
     */
    public Sequence() {
    }

    @Override
    public void mediate(Record record) {
        for (Mediator descendant : descendants()) {
            descendant.mediate(record);
        }
    }

}
