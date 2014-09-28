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

import hrytsenko.csv.core.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Aggregates all processed records.
 * 
 * @author hrytsenko.anton
 */
public class Aggregator extends Accumulator {

    private List<Record> records;

    /**
     * Creates empty aggregator.
     */
    public Aggregator() {
        records = new ArrayList<>();
    }

    @Override
    public void mediate(Record record) {
        records.add(record);
    }

    @Override
    protected Collection<Record> value() {
        return new ArrayList<>(records);
    }

}
