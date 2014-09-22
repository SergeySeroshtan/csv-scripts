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

import static org.junit.Assert.assertEquals;
import hrytsenko.csv.core.Record;

import org.junit.Before;
import org.junit.Test;

public class CounterTest {

    private static final int RECORDS_NUM = 3;
    private static final String NAME = "total";

    private Counter counter;

    @Before
    public void init() {
        counter = new Counter();
        counter.into(NAME);
    }

    @Test
    public void test() {

        for (int i = 0; i < RECORDS_NUM; ++i) {
            counter.mediate(new Record());
        }

        assertEquals(RECORDS_NUM, counter.getAt(NAME));
    }

}
