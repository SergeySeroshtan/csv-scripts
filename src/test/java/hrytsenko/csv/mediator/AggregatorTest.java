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

import static org.junit.Assert.assertArrayEquals;
import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AggregatorTest {

    private static final int RECORDS_NUM = 3;
    private static final String NAME = "records";

    private Aggregator aggregator;
    private List<Record> records;

    @Before
    public void init() {
        aggregator = new Aggregator();
        aggregator.into(NAME);

        records = new ArrayList<>();
        for (int i = 0; i < RECORDS_NUM; ++i) {
            records.add(new Record());
        }
    }

    @Test
    public void test() {
        for (Record record : records) {
            aggregator.mediate(record);
        }

        @SuppressWarnings("unchecked")
        Collection<Record> aggregated = (Collection<Record>) aggregator.getAt(NAME);
        assertArrayEquals(records.toArray(), aggregated.toArray());
    }

}
