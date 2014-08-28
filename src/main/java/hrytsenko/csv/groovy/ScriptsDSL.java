package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;
import hrytsenko.csv.mediator.Aggregator;
import hrytsenko.csv.mediator.Filter;
import hrytsenko.csv.mediator.Sequence;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Basic operations for scripts.
 * 
 * @author hrytsenko.anton
 */
public final class ScriptsDSL {

    /**
     * Creates {@link Aggregator} for records.
     * 
     * @return the created mediator.
     */
    public static Aggregator aggregate() {
        return new Aggregator();
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
     *            the mediators to be grouped into sequence.
     * 
     * @return the created mediator.
     */
    public static Sequence sequence(Mediator... mediators) {
        return new Sequence().of(mediators);
    }

    /**
     * Merges records from one or more sets of records.
     * 
     * @param idField
     *            the name of field, which value can be used as unique identifier.
     * @param sets
     *            the sets of records to be merged.
     * 
     * @return the resulting set of records.
     */
    @SafeVarargs
    public static Collection<Record> merge(String idField, Collection<Record>... sets) {
        Map<String, Record> result = new LinkedHashMap<>();
        for (Collection<Record> set : sets) {
            for (Record record : set) {
                String id = record.get(idField);
                result.put(id, merge(record, result.get(id)));
            }
        }
        return result.values();
    }

    private static Record merge(Record source, Record target) {
        if (target == null) {
            return new Record(source.content());
        }

        Record result = new Record(target.content());

        for (String field : source.fields()) {
            result.set(field, source.get(field));
        }
        return result;
    }

    private ScriptsDSL() {
    }

}
