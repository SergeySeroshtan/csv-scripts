package hrytsenko.csv.core;

/**
 * Conditions are intended to check that record meets criteria.
 * 
 * @author hrytsenko.anton
 */
public interface Condition {

    /**
     * Checks that record meets criteria.
     * 
     * @param record
     *            the record to be checked.
     * 
     * @return <code>true</code> if record meets criteria; <code>false</code> otherwise.
     */
    boolean check(Record record);

    /**
     * Condition that ignores all records.
     * 
     * @author hrytsenko.anton
     */
    class IgnoreAll implements Condition {

        @Override
        public boolean check(Record record) {
            return false;
        }

    }

}
