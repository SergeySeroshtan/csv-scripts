package hrytsenko.csv.mediator;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class AccumulatorTest {

    private static final String NAME = "test";

    private Accumulator accumulator;

    @Before
    public void init() {
        accumulator = mock(Accumulator.class, CALLS_REAL_METHODS);
        doReturn(null).when(accumulator).value();
    }

    @Test
    public void test() {
        accumulator.getAt(NAME);

        verify(accumulator, never()).value();

        accumulator.into(NAME);
        accumulator.getAt(NAME);

        verify(accumulator).value();
    }

}
