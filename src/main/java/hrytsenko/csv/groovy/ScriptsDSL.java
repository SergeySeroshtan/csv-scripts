package hrytsenko.csv.groovy;

import hrytsenko.csv.Condition;
import hrytsenko.csv.Mediator;
import hrytsenko.csv.Record;
import hrytsenko.csv.mediator.AggregateMediator;
import hrytsenko.csv.mediator.FilterMediator;
import hrytsenko.csv.mediator.SequenceMediator;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ScriptsDSL {

    public static AggregateMediator aggregate() {
        return new AggregateMediator();
    }

    public static FilterMediator filter(Condition condition) {
        return new FilterMediator(condition);
    }

    public static SequenceMediator sequence(Mediator... mediators) {
        return new SequenceMediator(mediators);
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
