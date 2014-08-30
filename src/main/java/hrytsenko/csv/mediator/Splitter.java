package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Splits copies of record between mediators.
 * 
 * @author hrytsenko.anton
 */
public class Splitter extends Container {

    /**
     * Creates empty splitter.
     */
    public Splitter() {
    }

    @Override
    public void mediate(Record record) {
        for (Mediator descendant : descendants()) {
            Record copy = new Record(record.content());
            descendant.mediate(copy);
        }
    }

}
