package hrytsenko.csv.mediator;

import static java.util.Arrays.asList;
import hrytsenko.csv.Mediator;
import hrytsenko.csv.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Sequence extends Accumulator {

    private List<Mediator> descendants;

    public Sequence() {
        this(new Mediator[] {});
    }

    public Sequence(Mediator... mediators) {
        this(asList(mediators));
    }

    public Sequence(Collection<Mediator> mediators) {
        this.descendants = new ArrayList<Mediator>(mediators);
    }

    public void add(Mediator mediator) {
        descendants.add(mediator);
    }

    @Override
    public void mediate(Record record) {
        for (Mediator mediator : descendants) {
            mediator.mediate(record);
        }
    }

    @Override
    public Collection<Mediator> descendants() {
        return descendants;
    }

}
