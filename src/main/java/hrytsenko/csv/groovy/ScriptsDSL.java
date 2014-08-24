package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;
import hrytsenko.csv.mediator.Aggregator;
import hrytsenko.csv.mediator.Filter;
import hrytsenko.csv.mediator.Sequence;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ScriptsDSL {

    public static Aggregator aggregate() {
        return new Aggregator();
    }

    public static Filter filter(Condition condition) {
        return new Filter(condition);
    }

    public static Sequence sequence(Mediator... mediators) {
        return new Sequence(mediators);
    }

    @SafeVarargs
    public static Collection<Record> merge(String idField, Collection<Record>... sets) throws IOException {
        Map<String, Record> result = new LinkedHashMap<String, Record>();
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
            result.set(field.toLowerCase(), source.get(field));
        }
        return result;
    }

    private ScriptsDSL() {
    }

}
