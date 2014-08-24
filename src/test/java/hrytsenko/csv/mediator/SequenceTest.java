package hrytsenko.csv.mediator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SequenceTest {

    private static final int DESCENDANTS_NUM = 3;

    private List<Mediator> descendants;

    private Sequence sequence;

    @Before
    public void init() {
        descendants = new ArrayList<Mediator>();
        for (int i = 0; i < DESCENDANTS_NUM; ++i) {
            Mediator descendant = mock(Mediator.class);
            descendants.add(descendant);
        }

        sequence = spy(new Sequence(descendants));
    }

    @Test
    public void test() {
        Record record = new Record();

        sequence.mediate(record);

        for (Mediator descendant : descendants) {
            verify(descendant).mediate(record);
        }
    }

}
