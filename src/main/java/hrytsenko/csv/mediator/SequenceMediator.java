package hrytsenko.csv.mediator;

import static java.util.Arrays.asList;
import hrytsenko.csv.Mediator;
import hrytsenko.csv.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequenceMediator extends Accumulator {

    private List<Mediator> sequence;

    public SequenceMediator() {
        this(new Mediator[] {});
    }

    public SequenceMediator(Mediator... mediators) {
        this(asList(mediators));
    }

    public SequenceMediator(Collection<Mediator> mediators) {
        this.sequence = new ArrayList<Mediator>(mediators);
    }

    public void add(Mediator mediator) {
        sequence.add(mediator);
    }

    @Override
    public void mediate(Record record) {
        for (Mediator mediator : sequence) {
            mediator.mediate(record);
        }
    }

    @Override
    public Collection<Mediator> children() {
        return sequence;
    }

}
