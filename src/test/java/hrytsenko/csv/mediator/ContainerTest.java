/*
 * #%L
 * csv-scripts
 * %%
 * Copyright (C) 2014 Anton Hrytsenko
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
