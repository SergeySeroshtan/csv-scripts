package hrytsenko.csv.mediator;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import org.junit.Before;
import org.junit.Test;

public class FilterTest {

    private Mediator descendant;

    private Condition condition;
    private Filter filter;

    @Before
    public void init() {
        descendant = mock(Mediator.class);

        condition = mock(Condition.class);
        when(condition.check(any(Record.class))).thenReturn(true, false);

        filter = spy(new Filter());
        filter.when(condition).over(descendant);
        ;
    }

    @Test
    public void test() {
        Record accepted = new Record();
        Record rejected = new Record();

        filter.mediate(accepted);
        filter.mediate(rejected);

        verify(condition).check(accepted);
        verify(descendant).mediate(accepted);

        verify(condition).check(rejected);
        verify(descendant, never()).mediate(rejected);
    }

}
