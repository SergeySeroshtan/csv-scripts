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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import org.junit.Before;
import org.junit.Test;

public class SequenceTest {

    private static final int DESCENDANTS_NUM = 3;

    private Mediator[] descendants;

    private Sequence sequence;

    @Before
    public void init() {
        descendants = new Mediator[DESCENDANTS_NUM];
        for (int i = 0; i < DESCENDANTS_NUM; ++i) {
            descendants[i] = mock(Mediator.class);
        }

        sequence = spy(new Sequence());
        sequence.over(descendants);
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
