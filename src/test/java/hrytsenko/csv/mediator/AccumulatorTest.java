package hrytsenko.csv.mediator;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
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
    }

    @Test
    public void testValue() {
        accumulator.pull(NAME);

        verify(accumulator).descendants();
        verify(accumulator, never()).value();
    }

    @Test
    public void testDescendants() {
        accumulator.into(NAME);

        accumulator.pull(NAME);

        verify(accumulator).value();
        verify(accumulator, never()).descendants();
    }

}
