/**
 * Copyright (C) 2014 Anton Hrytsenko (hrytsenko.anton@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
