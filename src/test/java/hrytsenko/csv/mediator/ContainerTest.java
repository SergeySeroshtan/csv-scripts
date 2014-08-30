package hrytsenko.csv.mediator;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import hrytsenko.csv.core.Mediator;

import org.junit.Before;
import org.junit.Test;

public class ContainerTest {
    private static final int DESCENDANTS_NUM = 3;

    private Mediator[] descendants;

    private Container container;

    @Before
    public void init() {
        descendants = new Mediator[DESCENDANTS_NUM];
        for (int i = 0; i < DESCENDANTS_NUM; ++i) {
            descendants[i] = mock(Mediator.class);
        }

        container = mock(Container.class, CALLS_REAL_METHODS);
    }

    @Test
    public void test() {
        container.over(descendants);

        assertArrayEquals(descendants, container.descendants().toArray());
    }

}
