package hrytsenko.csv.mediator;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import org.junit.Before;
import org.junit.Test;

public class SplitterTest {

    private static final int DESCENDANTS_NUM = 3;

    private Mediator[] descendants;

    private Splitter splitter;

    @Before
    public void init() {
        descendants = new Mediator[DESCENDANTS_NUM];
        for (int i = 0; i < DESCENDANTS_NUM; ++i) {
            descendants[i] = mock(Mediator.class);
        }

        splitter = spy(new Splitter());
        splitter.over(descendants);
    }

    @Test
    public void test() {
        Record record = new Record();

        splitter.mediate(record);

        for (Mediator descendant : descendants) {
            verify(descendant, never()).mediate(record);
            verify(descendant).mediate(any(Record.class));
        }
    }

}
